/*
 * Copyright 2012-2016 Tobi29
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

@file:Suppress("NOTHING_TO_INLINE")

package org.tobi29.scapes.engine.utils.graphics

import org.tobi29.scapes.engine.utils.math.abs

/**
 * Creates an array to give the offset for each sample
 * @param samples   Amount of samples per pixel
 * @param magnitude Maximum offset of samples
 * @return An array with a length of `samples`
 */
inline fun gaussianBlurOffset(samples: Int,
                              magnitude: Float): FloatArray {
    val sampleMagnitude = magnitude / samples.toFloat()
    val offset = samples shr 1
    val array = FloatArray(samples)
    for (sample in 0..samples - 1) {
        array[sample] = (sample - offset) * sampleMagnitude
    }
    return array
}

/**
 * Creates an array to give the weight for each sample
 * @param samples Amount of samples per pixel
 * @param curve   Modifier for each weight value
 * @return An array with a length of `samples`
 */
inline fun gaussianBlurWeight(samples: Int,
                              curve: (Double) -> Double): FloatArray {
    val scale = 1.0 / samples
    var magnitude = 0.0
    val offset = samples shr 1
    val array = FloatArray(samples)
    for (sample in 0..samples - 1) {
        val weight = curve.invoke(abs(sample - offset) * scale)
        array[sample] = weight.toFloat()
        magnitude += weight
    }
    for (sample in 0..samples - 1) {
        array[sample] /= magnitude.toFloat()
    }
    return array
}
