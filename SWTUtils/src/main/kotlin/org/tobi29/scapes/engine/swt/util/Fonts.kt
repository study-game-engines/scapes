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

package org.tobi29.scapes.engine.swt.util

import mu.KLogging
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.FontData
import org.tobi29.scapes.engine.utils.toArray
import java.io.IOException
import java.util.*

object Fonts : KLogging() {
    private val MONOSPACED = fontDatas("Monospaced")

    val monospace: Array<FontData>
        get() = Arrays.copyOf(MONOSPACED, MONOSPACED.size)

    private fun fontDatas(font: String): Array<FontData> {
        try {
            val classLoader = Fonts::class.java.classLoader
            val properties = Properties()
            classLoader.getResourceAsStream(
                    "fonts/$font.properties").use { streamIn ->
                properties.load(streamIn)
            }
            val os = identifier(System.getProperty("os.name"))
            val ws = identifier(SWT.getPlatform())
            var fonts: String? = properties.getProperty(os + '_' + ws)
            if (fonts == null) {
                fonts = properties.getProperty(os)
            }
            if (fonts == null) {
                logger.warn { "Unable to identify OS, using fallback." }
                fonts = properties.getProperty("unknown").orEmpty()
            }
            val fontDataTexts = fonts.split(';')
            return fontDataTexts.asSequence().map { text ->
                val split = text.split('|', limit = 3)
                assert(split.size == 3)
                val name = split[0]
                val styles = split[1].split(',')
                var style = 0
                for (s in styles) {
                    when (s) {
                        "normal" -> style = style or SWT.NORMAL
                        "bold" -> style = style or SWT.BOLD
                        "italic" -> style = style or SWT.ITALIC
                    }
                }
                val height = split[2].toInt()
                FontData(name, height, style)
            }.toArray()
        } catch (e: IOException) {
            logger.error { "Failed to load font: $e" }
        }
        return arrayOf()
    }

    private fun identifier(str: String): String {
        val characters = CharArray(str.length)
        var i = 0
        for (j in 0..str.length - 1) {
            val character = str[j]
            if (!Character.isWhitespace(character)) {
                characters[i++] = Character.toLowerCase(character)
            }
        }
        return String(characters, 0, i)
    }
}
