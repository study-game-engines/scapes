/*
 * Copyright 2012-2015 Tobi29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tobi29.scapes.engine.backends.lwjgl3.glfw;

import java8.util.Optional;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tobi29.scapes.engine.ScapesEngine;
import org.tobi29.scapes.engine.ScapesEngineException;
import org.tobi29.scapes.engine.backends.lwjgl3.ContainerLWJGL3;
import org.tobi29.scapes.engine.backends.lwjgl3.GLFWControllers;
import org.tobi29.scapes.engine.backends.lwjgl3.GLFWKeyMap;
import org.tobi29.scapes.engine.backends.lwjgl3.STBGlyphRenderer;
import org.tobi29.scapes.engine.backends.lwjgl3.glfw.spi.GLFWDialogsProvider;
import org.tobi29.scapes.engine.gui.GlyphRenderer;
import org.tobi29.scapes.engine.gui.GuiComponent;
import org.tobi29.scapes.engine.gui.GuiController;
import org.tobi29.scapes.engine.input.ControllerJoystick;
import org.tobi29.scapes.engine.input.ControllerKey;
import org.tobi29.scapes.engine.input.ControllerTouch;
import org.tobi29.scapes.engine.input.FileType;
import org.tobi29.scapes.engine.opengl.GraphicsCheckException;
import org.tobi29.scapes.engine.opengl.GraphicsException;
import org.tobi29.scapes.engine.utils.BufferCreatorNative;
import org.tobi29.scapes.engine.utils.DesktopException;
import org.tobi29.scapes.engine.utils.Pair;
import org.tobi29.scapes.engine.utils.Sync;
import org.tobi29.scapes.engine.utils.io.IOBiConsumer;
import org.tobi29.scapes.engine.utils.io.ReadableByteStream;
import org.tobi29.scapes.engine.utils.io.filesystem.FilePath;
import org.tobi29.scapes.engine.utils.io.tag.TagStructure;
import org.tobi29.scapes.engine.utils.profiler.Profiler;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ContainerGLFW extends ContainerLWJGL3 {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ContainerGLFW.class);
    private static final GLFWDialogsProvider DIALOGS_PROVIDER = loadDialogs();
    private final PlatformDialogs dialogs;
    private final Sync sync;
    private final GLFWControllers controllers;
    private final Map<Integer, ControllerJoystick> virtualJoysticks =
            new ConcurrentHashMap<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final GLFWErrorCallback errorFun;
    private final GLFWWindowSizeCallback windowSizeFun;
    private final GLFWWindowCloseCallback windowCloseFun;
    private final GLFWWindowFocusCallback windowFocusFun;
    private final GLFWFramebufferSizeCallback frameBufferSizeFun;
    private final GLFWKeyCallback keyFun;
    private final GLFWCharCallback charFun;
    private final GLFWMouseButtonCallback mouseButtonFun;
    private final GLFWCursorPosCallback cursorPosFun;
    private final GLFWScrollCallback scrollFun;
    private long window;
    private boolean running = true, skipMouseCallback, mouseGrabbed,
            mouseGrabbedCurrent;

    public ContainerGLFW(ScapesEngine engine) {
        super(engine);
        dialogs = DIALOGS_PROVIDER.createDialogs(engine);
        errorFun = GLFWErrorCallback.createPrint();
        GLFW.glfwSetErrorCallback(errorFun);
        if (GLFW.glfwInit() != GL11.GL_TRUE) {
            throw new GraphicsException("Unable to initialize GLFW");
        }
        LOGGER.info("GLFW version: {}", GLFW.glfwGetVersionString());
        sync = new Sync(engine.config().fps(), 5000000000L, false, "Rendering");
        controllers = new GLFWControllers(virtualJoysticks);
        windowSizeFun =
                GLFWWindowSizeCallback.create((window, width, height) -> {
                    containerWidth = width;
                    containerHeight = height;
                    containerResized = true;
                });
        windowCloseFun =
                GLFWWindowCloseCallback.create(window -> engine.stop());
        windowFocusFun = GLFWWindowFocusCallback
                .create((window, focused) -> focus = focused == GL11.GL_TRUE);
        frameBufferSizeFun =
                GLFWFramebufferSizeCallback.create((window, width, height) -> {
                    contentWidth = width;
                    contentHeight = height;
                    containerResized = true;
                });
        keyFun = GLFWKeyCallback
                .create((window, key, scancode, action, mods) -> {
                    ControllerKey virtualKey = GLFWKeyMap.key(key);
                    if (virtualKey != null) {
                        if (virtualKey == ControllerKey.KEY_BACKSPACE &&
                                action != GLFW.GLFW_RELEASE) {
                            addTypeEvent((char) 127);
                        }
                        switch (action) {
                            case GLFW.GLFW_PRESS:
                                addPressEvent(virtualKey, PressState.PRESS);
                                break;
                            case GLFW.GLFW_REPEAT:
                                addPressEvent(virtualKey, PressState.REPEAT);
                                break;
                            case GLFW.GLFW_RELEASE:
                                addPressEvent(virtualKey, PressState.RELEASE);
                                break;
                        }
                    }
                });
        charFun = GLFWCharCallback
                .create((window, codepoint) -> addTypeEvent((char) codepoint));
        mouseButtonFun = GLFWMouseButtonCallback
                .create((window, button, action, mods) -> {
                    ControllerKey virtualKey = ControllerKey.button(button);
                    if (virtualKey != ControllerKey.UNKNOWN) {
                        switch (action) {
                            case GLFW.GLFW_PRESS:
                                addPressEvent(virtualKey, PressState.PRESS);
                                break;
                            case GLFW.GLFW_RELEASE:
                                addPressEvent(virtualKey, PressState.RELEASE);
                                break;
                        }
                    }
                });
        cursorPosFun = GLFWCursorPosCallback.create((window, xpos, ypos) -> {
            if (skipMouseCallback) {
                skipMouseCallback = false;
                if (mouseGrabbed) {
                    GLFW.glfwSetCursorPos(window, 0.0, 0.0);
                }
            } else {
                double dx, dy;
                if (mouseGrabbed) {
                    dx = xpos;
                    dy = ypos;
                    GLFW.glfwSetCursorPos(window, 0.0, 0.0);
                } else {
                    dx = xpos - mouseX;
                    dy = ypos - mouseY;
                    mouseX = (int) xpos;
                    mouseY = (int) ypos;
                }
                if (dx != 0.0 || dy != 0.0) {
                    set(xpos, ypos);
                    addDelta(dx, dy);
                }
            }
        });
        scrollFun = GLFWScrollCallback.create((window, xoffset, yoffset) -> {
            if (xoffset != 0.0 || yoffset != 0.0) {
                addScroll(xoffset, yoffset);
            }
        });
    }

    private static GLFWDialogsProvider loadDialogs() {
        for (GLFWDialogsProvider dialogs : ServiceLoader
                .load(GLFWDialogsProvider.class)) {
            try {
                if (dialogs.available()) {
                    LOGGER.debug("Loaded dialogs: {}",
                            dialogs.getClass().getName());
                    return dialogs;
                }
            } catch (ServiceConfigurationError e) {
                LOGGER.warn("Unable to load dialogs provider: {}",
                        e.toString());
            }
        }
        throw new ScapesEngineException("No dialogs found!");
    }

    @Override
    public FormFactor formFactor() {
        return FormFactor.DESKTOP;
    }

    @Override
    public void setMouseGrabbed(boolean value) {
        mouseGrabbed = value;
    }

    @Override
    public void update(double delta) {
        if (isPressed(ControllerKey.KEY_F2)) {
            engine.graphics().triggerScreenshot();
        }
        if (engine.debug() && isPressed(ControllerKey.KEY_F3)) {
            GuiComponent debugValues = engine.debugValues();
            debugValues.setVisible(!debugValues.isVisible());
        }
        if (engine.debug() && isPressed(ControllerKey.KEY_F4)) {
            GuiComponent profiler = engine.profiler();
            profiler.setVisible(!profiler.isVisible());
        }
    }

    @Override
    public Collection<ControllerJoystick> joysticks() {
        joysticksChanged.set(false);
        Collection<ControllerJoystick> collection =
                new ArrayList<>(virtualJoysticks.size());
        collection.addAll(virtualJoysticks.values());
        return collection;
    }

    @Override
    public Optional<ControllerTouch> touch() {
        return Optional.empty();
    }

    @Override
    public Optional<String> loadFont(String asset) {
        return STBGlyphRenderer.loadFont(engine.files().get(asset + ".ttf"));
    }

    @Override
    public GlyphRenderer createGlyphRenderer(String fontName, int size) {
        return STBGlyphRenderer.fromFont(fontName, size);
    }

    @Override
    public void run() throws DesktopException {
        sync.init();
        while (running) {
            while (!tasks.isEmpty()) {
                tasks.poll().run();
            }
            if (!valid) {
                if (window != 0) {
                    engine.graphics().reset();
                    cleanWindow();
                }
                initWindow(engine.config().isFullscreen(),
                        engine.config().vSync());
                Optional<String> check = initContext();
                if (check.isPresent()) {
                    throw new GraphicsCheckException(check.get());
                }
                valid = true;
                containerResized = true;
            }
            boolean mouseGrabbed = this.mouseGrabbed;
            if (mouseGrabbed != mouseGrabbedCurrent) {
                mouseGrabbedCurrent = mouseGrabbed;
                if (mouseGrabbed) {
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
                            GLFW.GLFW_CURSOR_DISABLED);
                    GLFW.glfwSetCursorPos(window, 0.0, 0.0);
                    mouseX = 0.0;
                    mouseY = 0.0;
                    skipMouseCallback = true;
                } else {
                    mouseX = containerWidth * 0.5;
                    mouseY = containerHeight * 0.5;
                    GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
                            GLFW.GLFW_CURSOR_NORMAL);
                    GLFW.glfwSetCursorPos(window, mouseX, mouseY);
                }
            }
            GLFW.glfwPollEvents();
            if (controllers.poll()) {
                joysticksChanged.set(true);
            }
            try (Profiler.C ignored = Profiler.section("Render")) {
                engine.graphics().render(sync.delta());
            }
            containerResized = false;
            sync.cap();
            GLFW.glfwSwapBuffers(window);
            if (!visible) {
                GLFW.glfwShowWindow(window);
                visible = true;
            }
        }
        LOGGER.info("Disposing graphics system");
        engine.graphics().dispose();
        engine.dispose();
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        windowSizeFun.release();
        windowCloseFun.release();
        windowFocusFun.release();
        frameBufferSizeFun.release();
        keyFun.release();
        charFun.release();
        mouseButtonFun.release();
        cursorPosFun.release();
        scrollFun.release();
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void clipboardCopy(String value) {
        GLFW.glfwSetClipboardString(window, value);
    }

    @Override
    public String clipboardPaste() {
        return GLFW.glfwGetClipboardString(window);
    }

    @Override
    public void openFileDialog(FileType type, String title, boolean multiple,
            IOBiConsumer<String, ReadableByteStream> result)
            throws IOException {
        execIO(() -> dialogs
                .openFileDialog(window, type.extensions(), title, multiple,
                        result));
    }

    @Override
    public Optional<FilePath> saveFileDialog(Pair<String, String>[] extensions,
            String title) {
        return exec(() -> dialogs.saveFileDialog(window, extensions, title));
    }

    @Override
    public void message(MessageType messageType, String title, String message) {
        exec(() -> dialogs.message(window, messageType, title, message));
    }

    @Override
    public void dialog(String title, GuiController.TextFieldData text,
            boolean multiline) {
        exec(() -> dialogs.dialog(window, title, text, multiline));
    }

    @Override
    public void openFile(FilePath path) {
        exec(() -> dialogs.openFile(window, path));
    }

    protected void initWindow(boolean fullscreen, boolean vSync)
            throws GraphicsCheckException {
        LOGGER.info("Creating GLFW window...");
        String title = engine.game().name();
        long monitor = GLFW.glfwGetPrimaryMonitor();
        IntBuffer xBuffer = BufferCreatorNative.intsD(1);
        IntBuffer yBuffer = BufferCreatorNative.intsD(1);
        GLFW.glfwGetMonitorPos(monitor, xBuffer, yBuffer);
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
        int monitorX = xBuffer.get(0);
        int monitorY = yBuffer.get(0);
        int monitorWidth = videoMode.width();
        int monitorHeight = videoMode.height();
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        // >:V Seriously, stop with this crap!
        GLFW.glfwWindowHint(GLFW.GLFW_AUTO_ICONIFY, GL11.GL_FALSE);
        TagStructure tagStructure = engine.tagStructure();
        if (!tagStructure.has("Compatibility") ||
                !engine.tagStructure().getStructure("Compatibility")
                        .getBoolean("ForceLegacyGL")) {
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE,
                    GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
        }
        if (fullscreen) {
            window = GLFW.glfwCreateWindow(monitorWidth, monitorHeight, title,
                    monitor, 0L);
            if (window == 0) {
                throw new GraphicsCheckException(
                        "Failed to create fullscreen window");
            }
        } else {
            int width, height;
            if (monitorWidth > 1280 && monitorHeight > 720) {
                width = 1280;
                height = 720;
            } else {
                width = 960;
                height = 540;
            }
            window = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
            if (window == 0) {
                throw new GraphicsCheckException("Failed to create window");
            }
            GLFW.glfwSetWindowPos(window, monitorX + (monitorWidth - width) / 2,
                    monitorY + (monitorHeight - height) / 2);
        }
        IntBuffer widthBuffer = BufferCreatorNative.intsD(1);
        IntBuffer heightBuffer = BufferCreatorNative.intsD(1);
        GLFW.glfwGetWindowSize(window, widthBuffer, heightBuffer);
        containerWidth = widthBuffer.get(0);
        containerHeight = heightBuffer.get(0);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        contentWidth = widthBuffer.get(0);
        contentHeight = heightBuffer.get(0);
        GLFW.glfwSetWindowSizeCallback(window, windowSizeFun);
        GLFW.glfwSetWindowCloseCallback(window, windowCloseFun);
        GLFW.glfwSetWindowFocusCallback(window, windowFocusFun);
        GLFW.glfwSetFramebufferSizeCallback(window, frameBufferSizeFun);
        GLFW.glfwSetKeyCallback(window, keyFun);
        GLFW.glfwSetCharCallback(window, charFun);
        GLFW.glfwSetMouseButtonCallback(window, mouseButtonFun);
        GLFW.glfwSetCursorPosCallback(window, cursorPosFun);
        GLFW.glfwSetScrollCallback(window, scrollFun);
        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(vSync ? 1 : 0);
    }

    protected void cleanWindow() {
        clearStates();
        GLFW.glfwDestroyWindow(window);
        window = 0;
        visible = false;
    }
}
