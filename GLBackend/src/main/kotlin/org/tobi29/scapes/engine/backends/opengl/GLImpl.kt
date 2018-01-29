/*
 * Copyright 2012-2017 Tobi29
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
package org.tobi29.scapes.engine.backends.opengl

import org.tobi29.graphics.Image
import org.tobi29.graphics.flipVertical
import org.tobi29.io.ByteViewRO
import org.tobi29.scapes.engine.graphics.BlendingMode
import org.tobi29.scapes.engine.graphics.GL
import org.tobi29.scapes.engine.graphics.GraphicsException

class GLImpl(private val glh: GLHandle) : GL(glh) {
    override fun checkError(message: String) {
        val error = glh.glGetError()
        if (error != GL_NO_ERROR) {
            val errorName = when (error) {
                GL_INVALID_ENUM -> "Enum argument out of range"
                GL_INVALID_VALUE -> "Numeric argument out of range"
                GL_INVALID_OPERATION -> "Operation illegal in current state"
                GL_STACK_OVERFLOW -> "Command would cause a stack overflow"
                GL_STACK_UNDERFLOW -> "Command would cause a stack underflow"
                GL_OUT_OF_MEMORY -> "Not enough memory left to execute command"
                GL_INVALID_FRAMEBUFFER_OPERATION -> "Framebuffer object is not complete"
                GL_TABLE_TOO_LARGE -> "The specified table is too large"
                else -> "Unknown error code"
            }
            throw GraphicsException("$errorName in $message")
        }
    }

    override fun clear(r: Float, g: Float, b: Float, a: Float) {
        glh.glClearColor(r, g, b, a)
        glh.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    override fun clearDepth() {
        glh.glClear(GL_DEPTH_BUFFER_BIT)
    }

    override fun disableCulling() {
        glh.glDisable(GL_CULL_FACE)
    }

    override fun disableDepthTest() {
        glh.glDisable(GL_DEPTH_TEST)
    }

    override fun disableDepthMask() {
        glh.glDepthMask(false)
    }

    override fun disableWireframe() {
        glh.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
    }

    override fun disableScissor() {
        glh.glDisable(GL_SCISSOR_TEST)
    }

    override fun enableCulling() {
        glh.glEnable(GL_CULL_FACE)
    }

    override fun enableDepthTest() {
        glh.glEnable(GL_DEPTH_TEST)
        glh.glDepthFunc(GL_LEQUAL)
    }

    override fun enableDepthMask() {
        glh.glDepthMask(true)
    }

    override fun enableWireframe() {
        glh.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
    }

    override fun enableScissor(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) {
        glh.glEnable(GL_SCISSOR_TEST)
        glh.glScissor(x, contentHeight - y - height, width, height)
    }

    override fun setBlending(mode: BlendingMode) {
        when (mode) {
            BlendingMode.NONE -> glh.glDisable(GL_BLEND)
            BlendingMode.NORMAL -> {
                glh.glEnable(GL_BLEND)
                glh.glBlendFunc(
                    GL_SRC_ALPHA,
                    GL_ONE_MINUS_SRC_ALPHA
                )
            }
            BlendingMode.ADD -> {
                glh.glEnable(GL_BLEND)
                glh.glBlendFunc(GL_SRC_ALPHA, GL_DST_ALPHA)
            }
            BlendingMode.INVERT -> {
                glh.glEnable(GL_BLEND)
                glh.glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR)
            }
        }
    }

    override fun setViewport(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) {
        glh.glViewport(x, y, width, height)
    }

    override fun getViewport(output: IntArray) {
        glh.glGetIntegerv(GL_VIEWPORT, output)
    }

    override fun screenShot(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Image {
        glh.glReadBuffer(GL_FRONT)
        return readPixels(x, y, width, height)
    }

    override fun screenShotFBOColor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        attachment: Int
    ): Image {
        glh.glReadBuffer(GL_COLOR_ATTACHMENT(attachment))
        return readPixels(x, y, width, height)
    }

    override fun screenShotFBODepth(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Image {
        glh.glReadBuffer(GL_DEPTH_ATTACHMENT)
        return readPixels(x, y, width, height)
    }

    private fun readPixels(x: Int, y: Int, width: Int, height: Int): Image {
        val buffer = glh.byteView(width * height shl 2)
        glh.glReadPixels(
            x, y, width, height,
            GL_RGBA,
            GL_UNSIGNED_BYTE, buffer
        )
        flipVertical(width, height, buffer)
        return Image(width, height, buffer)
    }

    override fun setAttribute1f(
        id: Int,
        v0: Float
    ) {
        glh.glVertexAttrib1f(id, v0)
    }

    override fun setAttribute2f(
        id: Int,
        v0: Float,
        v1: Float
    ) {
        glh.glVertexAttrib2f(id, v0, v1)
    }

    override fun setAttribute3f(
        id: Int,
        v0: Float,
        v1: Float,
        v2: Float
    ) {
        glh.glVertexAttrib3f(id, v0, v1, v2)
    }

    override fun setAttribute4f(
        id: Int,
        v0: Float,
        v1: Float,
        v2: Float,
        v3: Float
    ) {
        glh.glVertexAttrib4f(id, v0, v1, v2, v3)
    }

    override fun setAttribute1f(
        uniform: Int,
        values: FloatArray
    ) {
        glh.glVertexAttrib1fv(uniform, values)
    }

    override fun setAttribute2f(
        uniform: Int,
        values: FloatArray
    ) {
        glh.glVertexAttrib2fv(uniform, values)
    }

    override fun setAttribute3f(
        uniform: Int,
        values: FloatArray
    ) {
        glh.glVertexAttrib3fv(uniform, values)
    }

    override fun setAttribute4f(
        uniform: Int,
        values: FloatArray
    ) {
        glh.glVertexAttrib4fv(uniform, values)
    }

    override fun replaceTexture(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        buffer: ByteViewRO
    ) {
        glh.glTexSubImage2D(
            GL_TEXTURE_2D, 0, x, y, width, height, GL_RGBA,
            GL_UNSIGNED_BYTE, buffer
        )
    }

    override fun replaceTextureMipMap(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        vararg buffers: ByteViewRO
    ) {
        glh.glTexSubImage2D(
            GL_TEXTURE_2D, 0, x, y, width, height,
            GL_RGBA, GL_UNSIGNED_BYTE, buffers[0]
        )
        for (i in 1 until buffers.size) {
            val scale = 1 shl i
            glh.glTexSubImage2D(
                GL_TEXTURE_2D, i, x / scale, y / scale,
                (width / scale).coerceAtLeast(1),
                (height / scale).coerceAtLeast(1), GL_RGBA,
                GL_UNSIGNED_BYTE, buffers[i]
            )
        }
    }

    override fun activeTexture(i: Int) {
        glh.glActiveTexture(GL_TEXTURE(i))
    }
}
