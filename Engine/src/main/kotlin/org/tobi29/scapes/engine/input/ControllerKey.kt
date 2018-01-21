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

import org.tobi29.stdex.ConcurrentHashMap
import org.tobi29.stdex.computeAbsent

class ControllerKey private constructor(val name: String,
                                        val humanName: String) {
    override fun toString() = name

    companion object {
        private val keyCache = ConcurrentHashMap<String, ControllerKey>()

        val KEY_SPACE = of("KEY_SPACE", "Space")
        val KEY_APOSTROPHE = of("KEY_APOSTROPHE", "Apostrophe")
        val KEY_COMMA = of("KEY_COMMA", "Comma")
        val KEY_MINUS = of("KEY_MINUS", "Minus")
        val KEY_PERIOD = of("KEY_PERIOD", "Period")
        val KEY_SLASH = of("KEY_SLASH", "Slash")
        val KEY_0 = of("KEY_0", "0")
        val KEY_1 = of("KEY_1", "1")
        val KEY_2 = of("KEY_2", "2")
        val KEY_3 = of("KEY_3", "3")
        val KEY_4 = of("KEY_4", "4")
        val KEY_5 = of("KEY_5", "5")
        val KEY_6 = of("KEY_6", "6")
        val KEY_7 = of("KEY_7", "7")
        val KEY_8 = of("KEY_8", "8")
        val KEY_9 = of("KEY_9", "9")
        val KEY_SEMICOLON = of("KEY_SEMICOLON", "Semicolon")
        val KEY_EQUAL = of("KEY_EQUAL", "Equal")
        val KEY_A = of("KEY_A", "A")
        val KEY_B = of("KEY_B", "B")
        val KEY_C = of("KEY_C", "C")
        val KEY_D = of("KEY_D", "D")
        val KEY_E = of("KEY_E", "E")
        val KEY_F = of("KEY_F", "F")
        val KEY_G = of("KEY_G", "G")
        val KEY_H = of("KEY_H", "H")
        val KEY_I = of("KEY_I", "I")
        val KEY_J = of("KEY_J", "J")
        val KEY_K = of("KEY_K", "K")
        val KEY_L = of("KEY_L", "L")
        val KEY_M = of("KEY_M", "M")
        val KEY_N = of("KEY_N", "N")
        val KEY_O = of("KEY_O", "O")
        val KEY_P = of("KEY_P", "P")
        val KEY_Q = of("KEY_Q", "Q")
        val KEY_R = of("KEY_R", "R")
        val KEY_S = of("KEY_S", "S")
        val KEY_T = of("KEY_T", "T")
        val KEY_U = of("KEY_U", "U")
        val KEY_V = of("KEY_V", "V")
        val KEY_W = of("KEY_W", "W")
        val KEY_X = of("KEY_X", "X")
        val KEY_Y = of("KEY_Y", "Y")
        val KEY_Z = of("KEY_Z", "Z")
        val KEY_BRACKET_LEFT = of("KEY_BRACKET_LEFT", "Left Bracket")
        val KEY_BRACKET_RIGHT = of("KEY_BRACKET_RIGHT", "Right Bracket")
        val KEY_BACKSLASH = of("KEY_BACKSLASH", "Backslash")
        val KEY_GRAVE_ACCENT = of("KEY_GRAVE_ACCENT", "Grave Accent")
        val KEY_ESCAPE = of("KEY_ESCAPE", "Escape")
        val KEY_ENTER = of("KEY_ENTER", "Enter")
        val KEY_TAB = of("KEY_TAB", "Tab")
        val KEY_BACKSPACE = of("KEY_BACKSPACE", "Backspace")
        val KEY_INSERT = of("KEY_INSERT", "Insert")
        val KEY_DELETE = of("KEY_DELETE", "Delete")
        val KEY_RIGHT = of("KEY_RIGHT", "Right")
        val KEY_LEFT = of("KEY_LEFT", "Left")
        val KEY_DOWN = of("KEY_DOWN", "Down")
        val KEY_UP = of("KEY_UP", "Up")
        val KEY_PAGE_UP = of("KEY_PAGE_UP", "Page Up")
        val KEY_PAGE_DOWN = of("KEY_PAGE_DOWN", "Page Down")
        val KEY_HOME = of("KEY_HOME", "Home")
        val KEY_END = of("KEY_END", "End")
        val KEY_CAPS_LOCK = of("KEY_CAPS_LOCK", "Caps Lock")
        val KEY_SCROLL_LOCK = of("KEY_SCROLL_LOCK", "Scroll Lock")
        val KEY_NUM_LOCK = of("KEY_NUM_LOCK", "Num Lock")
        val KEY_PRINT_SCREEN = of("KEY_PRINT_SCREEN", "Print Screen")
        val KEY_PAUSE = of("KEY_PAUSE", "Pause")
        val KEY_F1 = of("KEY_F1", "F1")
        val KEY_F2 = of("KEY_F2", "F2")
        val KEY_F3 = of("KEY_F3", "F3")
        val KEY_F4 = of("KEY_F4", "F4")
        val KEY_F5 = of("KEY_F5", "F5")
        val KEY_F6 = of("KEY_F6", "F6")
        val KEY_F7 = of("KEY_F7", "F7")
        val KEY_F8 = of("KEY_F8", "F8")
        val KEY_F9 = of("KEY_F9", "F9")
        val KEY_F10 = of("KEY_F10", "F10")
        val KEY_F11 = of("KEY_F11", "F11")
        val KEY_F12 = of("KEY_F12", "F12")
        val KEY_F13 = of("KEY_F13", "F13")
        val KEY_F14 = of("KEY_F14", "F14")
        val KEY_F15 = of("KEY_F15", "F15")
        val KEY_F16 = of("KEY_F16", "F16")
        val KEY_F17 = of("KEY_F17", "F17")
        val KEY_F18 = of("KEY_F18", "F18")
        val KEY_F19 = of("KEY_F19", "F19")
        val KEY_F20 = of("KEY_F20", "F20")
        val KEY_F21 = of("KEY_F21", "F21")
        val KEY_F22 = of("KEY_F22", "F22")
        val KEY_F23 = of("KEY_F23", "F23")
        val KEY_F24 = of("KEY_F24", "F24")
        val KEY_F25 = of("KEY_F25", "F25")
        val KEY_KP_0 = of("KEY_KP_0", "KP 0")
        val KEY_KP_1 = of("KEY_KP_1", "KP 1")
        val KEY_KP_2 = of("KEY_KP_2", "KP 2")
        val KEY_KP_3 = of("KEY_KP_3", "KP 3")
        val KEY_KP_4 = of("KEY_KP_4", "KP 4")
        val KEY_KP_5 = of("KEY_KP_5", "KP 5")
        val KEY_KP_6 = of("KEY_KP_6", "KP 6")
        val KEY_KP_7 = of("KEY_KP_7", "KP 7")
        val KEY_KP_8 = of("KEY_KP_8", "KP 8")
        val KEY_KP_9 = of("KEY_KP_9", "KP 9")
        val KEY_KP_DECIMAL = of("KEY_KP_DECIMAL", "KP Decimal")
        val KEY_KP_DIVIDE = of("KEY_KP_DIVIDE", "KP Divide")
        val KEY_KP_MULTIPLY = of("KEY_KP_MULTIPLY", "KP Multiply")
        val KEY_KP_SUBTRACT = of("KEY_KP_SUBTRACT", "KP Subtract")
        val KEY_KP_ADD = of("KEY_KP_ADD", "KP Add")
        val KEY_KP_ENTER = of("KEY_KP_ENTER", "KP Enter")
        val KEY_KP_EQUAL = of("KEY_KP_EQUAL", "KP Enter")
        val KEY_SHIFT_LEFT = of("KEY_SHIFT_LEFT", "Left Shift")
        val KEY_CONTROL_LEFT = of("KEY_CONTROL_LEFT", "Left Control")
        val KEY_ALT_LEFT = of("KEY_ALT_LEFT", "Left Alt")
        val KEY_SUPER_LEFT = of("KEY_SUPER_LEFT", "Left Super")
        val KEY_SHIFT_RIGHT = of("KEY_SHIFT_RIGHT", "Right Shift")
        val KEY_CONTROL_RIGHT = of("KEY_CONTROL_RIGHT", "Right Control")
        val KEY_ALT_RIGHT = of("KEY_ALT_RIGHT", "Right Alt")
        val KEY_SUPER_RIGHT = of("KEY_SUPER_RIGHT", "Right Super")
        val KEY_MENU = of("KEY_MENU", "Menu")
        val BUTTON_0 = of("BUTTON_0", "Button 0")
        val BUTTON_1 = of("BUTTON_1", "Button 1")
        val BUTTON_2 = of("BUTTON_2", "Button 2")
        val BUTTON_3 = of("BUTTON_3", "Button 3")
        val BUTTON_4 = of("BUTTON_4", "Button 4")
        val BUTTON_5 = of("BUTTON_5", "Button 5")
        val BUTTON_6 = of("BUTTON_6", "Button 6")
        val BUTTON_7 = of("BUTTON_7", "Button 7")
        val BUTTON_8 = of("BUTTON_8", "Button 8")
        val BUTTON_9 = of("BUTTON_9", "Button 9")
        val BUTTON_10 = of("BUTTON_10", "Button 10")
        val BUTTON_11 = of("BUTTON_11", "Button 11")
        val BUTTON_12 = of("BUTTON_12", "Button 12")
        val BUTTON_13 = of("BUTTON_13", "Button 13")
        val BUTTON_14 = of("BUTTON_14", "Button 14")
        val BUTTON_15 = of("BUTTON_15", "Button 15")
        val BUTTON_16 = of("BUTTON_16", "Button 16")
        val BUTTON_17 = of("BUTTON_17", "Button 17")
        val BUTTON_18 = of("BUTTON_18", "Button 18")
        val BUTTON_19 = of("BUTTON_19", "Button 19")
        val BUTTON_20 = of("BUTTON_20", "Button 20")
        val BUTTON_21 = of("BUTTON_21", "Button 21")
        val BUTTON_22 = of("BUTTON_22", "Button 22")
        val BUTTON_23 = of("BUTTON_23", "Button 23")
        val BUTTON_24 = of("BUTTON_24", "Button 24")
        val BUTTON_25 = of("BUTTON_25", "Button 25")
        val BUTTON_26 = of("BUTTON_26", "Button 26")
        val BUTTON_27 = of("BUTTON_27", "Button 27")
        val BUTTON_28 = of("BUTTON_28", "Button 28")
        val BUTTON_29 = of("BUTTON_29", "Button 29")
        val BUTTON_30 = of("BUTTON_30", "Button 30")
        val BUTTON_31 = of("BUTTON_31", "Button 31")
        val BUTTON_32 = of("BUTTON_32", "Button 32")
        val BUTTON_33 = of("BUTTON_33", "Button 33")
        val BUTTON_34 = of("BUTTON_34", "Button 34")
        val BUTTON_35 = of("BUTTON_35", "Button 35")
        val BUTTON_36 = of("BUTTON_36", "Button 36")
        val BUTTON_37 = of("BUTTON_37", "Button 37")
        val BUTTON_38 = of("BUTTON_38", "Button 38")
        val BUTTON_39 = of("BUTTON_39", "Button 39")
        val BUTTON_40 = of("BUTTON_40", "Button 40")
        val BUTTON_41 = of("BUTTON_41", "Button 41")
        val BUTTON_42 = of("BUTTON_42", "Button 42")
        val BUTTON_43 = of("BUTTON_43", "Button 43")
        val BUTTON_44 = of("BUTTON_44", "Button 44")
        val BUTTON_45 = of("BUTTON_45", "Button 45")
        val BUTTON_46 = of("BUTTON_46", "Button 46")
        val BUTTON_47 = of("BUTTON_47", "Button 47")
        val BUTTON_48 = of("BUTTON_48", "Button 48")
        val BUTTON_49 = of("BUTTON_49", "Button 49")
        val BUTTON_50 = of("BUTTON_50", "Button 50")
        val BUTTON_51 = of("BUTTON_51", "Button 51")
        val BUTTON_52 = of("BUTTON_52", "Button 52")
        val BUTTON_53 = of("BUTTON_53", "Button 53")
        val BUTTON_54 = of("BUTTON_54", "Button 54")
        val BUTTON_55 = of("BUTTON_55", "Button 55")
        val BUTTON_56 = of("BUTTON_56", "Button 56")
        val BUTTON_57 = of("BUTTON_57", "Button 57")
        val BUTTON_58 = of("BUTTON_58", "Button 58")
        val BUTTON_59 = of("BUTTON_59", "Button 59")
        val BUTTON_60 = of("BUTTON_60", "Button 60")
        val BUTTON_61 = of("BUTTON_61", "Button 61")
        val BUTTON_62 = of("BUTTON_62", "Button 62")
        val BUTTON_63 = of("BUTTON_63", "Button 63")
        val BUTTON_64 = of("BUTTON_64", "Button 64")
        val BUTTON_65 = of("BUTTON_65", "Button 65")
        val BUTTON_66 = of("BUTTON_66", "Button 66")
        val BUTTON_67 = of("BUTTON_67", "Button 67")
        val BUTTON_68 = of("BUTTON_68", "Button 68")
        val BUTTON_69 = of("BUTTON_69", "Button 69")
        val BUTTON_70 = of("BUTTON_70", "Button 70")
        val BUTTON_71 = of("BUTTON_71", "Button 71")
        val BUTTON_72 = of("BUTTON_72", "Button 72")
        val BUTTON_73 = of("BUTTON_73", "Button 73")
        val BUTTON_74 = of("BUTTON_74", "Button 74")
        val BUTTON_75 = of("BUTTON_75", "Button 75")
        val BUTTON_76 = of("BUTTON_76", "Button 76")
        val BUTTON_77 = of("BUTTON_77", "Button 77")
        val BUTTON_78 = of("BUTTON_78", "Button 78")
        val BUTTON_79 = of("BUTTON_79", "Button 79")
        val BUTTON_A = of("BUTTON_A", "Button A")
        val BUTTON_B = of("BUTTON_B", "Button B")
        val BUTTON_X = of("BUTTON_X", "Button X")
        val BUTTON_Y = of("BUTTON_Y", "Button Y")
        val BUTTON_BUMPER_LEFT = of("BUTTON_BUMPER_LEFT", "Left Bumper")
        val BUTTON_BUMPER_RIGHT = of("BUTTON_BUMPER_RIGHT", "Right Bumper")
        val BUTTON_BUMPER2_LEFT = of("BUTTON_BUMPER2_LEFT", "Left Bumper 2")
        val BUTTON_BUMPER2_RIGHT = of("BUTTON_BUMPER2_RIGHT", "Right Bumper 2")
        val BUTTON_TRIGGER_LEFT = of("BUTTON_TRIGGER_LEFT", "Left Trigger")
        val BUTTON_TRIGGER_RIGHT = of("BUTTON_TRIGGER_RIGHT", "Right Trigger")
        val BUTTON_START = of("BUTTON_START", "Start")
        val BUTTON_SELECT = of("BUTTON_SELECT", "Select")
        val BUTTON_OPTIONS = of("BUTTON_OPTIONS", "Options")
        val BUTTON_SHARE = of("BUTTON_SHARE", "Share")
        val BUTTON_THUMB_LEFT = of("BUTTON_THUMB_LEFT", "Left Thumb")
        val BUTTON_THUMB_RIGHT = of("BUTTON_THUMB_RIGHT", "Right Thumb")
        /**
         * That button to access the OS menu on consoles
         */
        val BUTTON_WHATEVER = of("BUTTON_WHATEVER", "The Whatever Button")
        /**
         * Touchpad button (Dualshock 4)
         */
        val BUTTON_TOUCHPAD = of("BUTTON_TOUCHPAD", "Touchpad")
        val BUTTON_DPAD_RIGHT = of("BUTTON_DPAD_RIGHT", "DPad Right")
        val BUTTON_DPAD_LEFT = of("BUTTON_DPAD_LEFT", "DPad Left")
        val BUTTON_DPAD_DOWN = of("BUTTON_DPAD_DOWN", "DPad Down")
        val BUTTON_DPAD_UP = of("BUTTON_DPAD_UP", "DPad Up")
        val SCROLL_DOWN = of("SCROLL_DOWN", "Scroll Down")
        val SCROLL_UP = of("SCROLL_UP", "Scroll Up")
        val SCROLL_LEFT = of("SCROLL_LEFT", "Scroll Left")
        val SCROLL_RIGHT = of("SCROLL_RIGHT", "Scroll Right")
        val AXIS_0 = of("AXIS_0", "Axis 0")
        val AXIS_1 = of("AXIS_1", "Axis 1")
        val AXIS_2 = of("AXIS_2", "Axis 2")
        val AXIS_3 = of("AXIS_3", "Axis 3")
        val AXIS_4 = of("AXIS_4", "Axis 4")
        val AXIS_5 = of("AXIS_5", "Axis 5")
        val AXIS_6 = of("AXIS_6", "Axis 6")
        val AXIS_7 = of("AXIS_7", "Axis 7")
        val AXIS_8 = of("AXIS_8", "Axis 8")
        val AXIS_9 = of("AXIS_9", "Axis 9")
        val AXIS_10 = of("AXIS_10", "Axis 10")
        val AXIS_11 = of("AXIS_11", "Axis 11")
        val AXIS_12 = of("AXIS_12", "Axis 12")
        val AXIS_13 = of("AXIS_13", "Axis 13")
        val AXIS_14 = of("AXIS_14", "Axis 14")
        val AXIS_15 = of("AXIS_15", "Axis 15")
        val AXIS_NEG_0 = of("AXIS_NEG_0", "Axis Negative 0")
        val AXIS_NEG_1 = of("AXIS_NEG_1", "Axis Negative 1")
        val AXIS_NEG_2 = of("AXIS_NEG_2", "Axis Negative 2")
        val AXIS_NEG_3 = of("AXIS_NEG_3", "Axis Negative 3")
        val AXIS_NEG_4 = of("AXIS_NEG_4", "Axis Negative 4")
        val AXIS_NEG_5 = of("AXIS_NEG_5", "Axis Negative 5")
        val AXIS_NEG_6 = of("AXIS_NEG_6", "Axis Negative 6")
        val AXIS_NEG_7 = of("AXIS_NEG_7", "Axis Negative 7")
        val AXIS_NEG_8 = of("AXIS_NEG_8", "Axis Negative 8")
        val AXIS_NEG_9 = of("AXIS_NEG_9", "Axis Negative 9")
        val AXIS_NEG_10 = of("AXIS_NEG_10", "Axis Negative 10")
        val AXIS_NEG_11 = of("AXIS_NEG_11", "Axis Negative 11")
        val AXIS_NEG_12 = of("AXIS_NEG_12", "Axis Negative 12")
        val AXIS_NEG_13 = of("AXIS_NEG_13", "Axis Negative 13")
        val AXIS_NEG_14 = of("AXIS_NEG_14", "Axis Negative 14")
        val AXIS_NEG_15 = of("AXIS_NEG_15", "Axis Negative 15")

        fun valueOf(name: String) = keyCache[name]

        fun of(name: String,
               humanName: String): ControllerKey =
                keyCache.computeAbsent(name) {
                    ControllerKey(name, humanName)
                }

        private val BUTTONS = Array(80) {
            ControllerKey.valueOf("BUTTON_$it")
        }
        private val AXES = Array(16) {
            ControllerKey.valueOf("AXIS_$it")
        }
        private val AXES_NEG = Array(16) {
            ControllerKey.valueOf("AXIS_NEG_$it")
        }

        fun button(i: Int) = BUTTONS.getOrNull(i)

        fun axis(i: Int) = AXES.getOrNull(i)

        fun axisNegative(i: Int) = AXES_NEG.getOrNull(i)
    }
}
