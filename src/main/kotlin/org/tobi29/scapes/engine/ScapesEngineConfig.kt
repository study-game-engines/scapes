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
package org.tobi29.scapes.engine

import org.tobi29.scapes.engine.utils.io.tag.TagStructure
import org.tobi29.scapes.engine.utils.io.tag.getDouble
import org.tobi29.scapes.engine.utils.io.tag.setDouble

class ScapesEngineConfig(private val tagStructure: TagStructure) {
    var fps = 0.0
        set(value) {
            field = value
            tagStructure.setDouble("Framerate", value)
        }
    var resolutionMultiplier = 0.0
        set(value) {
            field = value
            tagStructure.setDouble("ResolutionMultiplier", value)
        }
    var vSync = false
        set(value) {
            field = value
            tagStructure.setBoolean("VSync", value)
        }
    var fullscreen = false
        set(value) {
            field = value
            tagStructure.setBoolean("Fullscreen", value)
        }

    init {
        vSync = tagStructure.getBoolean("VSync") ?: false
        fps = tagStructure.getDouble("Framerate") ?: 0.0
        resolutionMultiplier = tagStructure.getDouble(
                "ResolutionMultiplier") ?: 0.0
        fullscreen = tagStructure.getBoolean("Fullscreen") ?: false
    }

    fun volume(channel: String) = (tagStructure.structure(
            "Volumes").tagEntrySet.asSequence().filter {
        channel.startsWith(it.key)
    }.maxBy { it.key.length }?.value as? Number)?.toDouble() ?: 1.0


    fun setVolume(channel: String,
                  value: Double) {
        tagStructure.structure("Volumes").setDouble(channel, value)
    }
}
