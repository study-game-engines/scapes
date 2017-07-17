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

package org.tobi29.scapes.engine.input

import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.utils.ConcurrentHashMap
import org.tobi29.scapes.engine.utils.ConcurrentLinkedQueue
import org.tobi29.scapes.engine.utils.EventDispatcher
import org.tobi29.scapes.engine.utils.math.vector.Vector2d
import org.tobi29.scapes.engine.utils.remove

abstract class ControllerDefault : ControllerBasic {
    private val states = ConcurrentHashMap<ControllerKey, KeyState>()
    private val pressEventQueue = ConcurrentLinkedQueue<ControllerBasic.PressEvent>()
    private val typeEventQueue = ConcurrentLinkedQueue<KeyTypeEvent>()
    private var pressEvents: Collection<ControllerBasic.PressEvent> = emptyList()
    private var typeEvents: Collection<KeyTypeEvent> = emptyList()
    private var x = 0.0
    private var y = 0.0
    private var deltaX = 0.0
    private var deltaY = 0.0
    private var scrollX = 0.0
    private var scrollY = 0.0
    private var deltaXSet = 0.0
    private var deltaYSet = 0.0
    private var deltaXEvent = 0.0
    private var deltaYEvent = 0.0
    private var scrollXSet = 0.0
    private var scrollYSet = 0.0
    override var isActive = false
        protected set

    override fun poll(events: EventDispatcher) {
        synchronized(this) {
            states.forEach {
                when (it.value) {
                    KeyState.PRESSED ->
                        states.replace(it.key, KeyState.PRESSED, KeyState.DOWN)
                    KeyState.RELEASED ->
                        states.remove(it.key, KeyState.RELEASED)
                }
            }
            val newPressEvents = ArrayList<ControllerBasic.PressEvent>()
            val newTypeEvents = ArrayList<KeyTypeEvent>()
            isActive = !pressEventQueue.isEmpty()
            while (!pressEventQueue.isEmpty()) {
                val event = pressEventQueue.poll()!!
                val key = event.key
                when (event.state) {
                    ControllerBasic.PressState.PRESS ->
                        states[key] = KeyState.PRESSED
                    ControllerBasic.PressState.RELEASE -> {
                        states.replace(key, KeyState.PRESSED, KeyState.RELEASED)
                        states.remove(key, KeyState.DOWN)
                    }
                }
                newPressEvents.add(event)
            }
            while (!typeEventQueue.isEmpty()) {
                newTypeEvents.add(typeEventQueue.poll()!!)
            }
            pressEvents = newPressEvents
            typeEvents = newTypeEvents
            deltaX = deltaXSet
            deltaXSet = 0.0
            deltaY = deltaYSet
            deltaYSet = 0.0
            scrollX = scrollXSet
            scrollXSet = 0.0
            scrollY = scrollYSet
            scrollYSet = 0.0
            if (deltaXEvent != 0.0 || deltaYEvent != 0.0) {
                events.fire(
                        MouseDeltaEvent(this,
                                Vector2d(deltaXEvent, deltaYEvent)))
                deltaXEvent = 0.0
                deltaYEvent = 0.0
            }
        }
    }

    override fun isDown(key: ControllerKey): Boolean {
        return states[key] != null
    }

    override fun isPressed(key: ControllerKey): Boolean {
        return (states[key] ?: return false) != KeyState.DOWN
    }

    override fun pressEvents(): Sequence<ControllerBasic.PressEvent> {
        return pressEvents.asSequence()
    }

    override fun addPressEvent(key: ControllerKey,
                               state: ControllerBasic.PressState) {
        pressEventQueue.add(ControllerBasic.PressEvent(key, state))
    }

    fun typeEvents(): Sequence<KeyTypeEvent> {
        return typeEvents.asSequence()
    }

    open val isModifierDown get() = isDown(ControllerKey.KEY_CONTROL_LEFT) ||
            isDown(ControllerKey.KEY_CONTROL_RIGHT)

    fun x(): Double {
        return x
    }

    fun y(): Double {
        return y
    }

    fun deltaX(): Double {
        return deltaX
    }

    fun deltaY(): Double {
        return deltaY
    }

    fun scrollX(): Double {
        return scrollX
    }

    fun scrollY(): Double {
        return scrollY
    }

    fun addTypeEvent(character: Char) {
        typeEventQueue.add(KeyTypeEvent(character))
    }

    fun clearTypeEvents() {
        typeEvents = emptyList<KeyTypeEvent>()
    }

    fun set(x: Double,
            y: Double) {
        this.x = x
        this.y = y
    }

    fun addDelta(x: Double,
                 y: Double,
                 engine: ScapesEngine? = null) {
        deltaXSet += x
        deltaYSet += y
        if (engine == null) {
            deltaXEvent += x
            deltaYEvent += y
        } else {
            engine.events.fire(MouseDeltaEvent(this, Vector2d(x, y)))
        }
    }

    fun addScroll(x: Double,
                  y: Double) {
        scrollXSet += x
        scrollYSet += y
        if (x < 0.0) {
            addPressEvent(ControllerKey.SCROLL_LEFT,
                    ControllerBasic.PressState.PRESS)
            addPressEvent(ControllerKey.SCROLL_LEFT,
                    ControllerBasic.PressState.RELEASE)
        } else if (x > 0.0) {
            addPressEvent(ControllerKey.SCROLL_RIGHT,
                    ControllerBasic.PressState.PRESS)
            addPressEvent(ControllerKey.SCROLL_RIGHT,
                    ControllerBasic.PressState.RELEASE)
        }
        if (y < 0.0) {
            addPressEvent(ControllerKey.SCROLL_DOWN,
                    ControllerBasic.PressState.PRESS)
            addPressEvent(ControllerKey.SCROLL_DOWN,
                    ControllerBasic.PressState.RELEASE)
        } else if (y > 0.0) {
            addPressEvent(ControllerKey.SCROLL_UP,
                    ControllerBasic.PressState.PRESS)
            addPressEvent(ControllerKey.SCROLL_UP,
                    ControllerBasic.PressState.RELEASE)
        }
    }

    fun clearStates() {
        synchronized(this) {
            states.clear()
        }
    }

    class KeyTypeEvent(private val character: Char) {

        fun character(): Char {
            return character
        }
    }

    class MouseDeltaEvent(val controller: ControllerDefault,
                          val delta: Vector2d)

    private enum class KeyState {
        DOWN,
        PRESSED,
        RELEASED
    }
}
