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

@file:Suppress("NOTHING_TO_INLINE")

package org.tobi29.scapes.engine.utils.math

import org.tobi29.scapes.engine.utils.math.vector.Vector2d
import org.tobi29.scapes.engine.utils.math.vector.Vector2i
import org.tobi29.scapes.engine.utils.math.vector.Vector3d
import org.tobi29.scapes.engine.utils.math.vector.Vector3i

/**
 * Estimate for `e`
 */
const val E = 2.7182818284590452354

/**
 * Estimate for `pi`
 */
const val PI = 3.14159265358979323846

/**
 * Estimate for `pi * 0.5`
 */
const val HALF_PI = PI * 0.5

/**
 * Estimate for `pi * 2.0`
 * Equivalent to the maximum of a full circle in radians
 */
const val TWO_PI = PI * 2.0

/**
 * Converts radians to degrees by multiplying
 */
const val RAD_2_DEG = 180.0 / PI

/**
 * Converts degrees into radians by multiplying
 */
const val DEG_2_RAD = PI / 180.0

/**
 * Returns the absolute values of [value]
 * @param value The value
 * @return [Vector2d] with absolute values of [value]
 */
inline fun abs(value: Vector2d): Vector2d {
    return Vector2d(abs(value.x), abs(value.y))
}

/**
 * Returns the absolute values of [value]
 * @param value The value
 * @return [Vector3d] with absolute values of [value]
 */
inline fun abs(value: Vector3d): Vector3d {
    return Vector3d(abs(value.x), abs(value.y),
            abs(value.z))
}

/*
/**
 * Returns the absolute value of [value]
 * @param value The value
 * @return Absolute value of [value]
 */
header fun abs(value: Int): Int

/**
 * Returns the absolute value of [value]
 * @param value The value
 * @return Absolute value of [value]
 */
header fun abs(value: Long): Long

/**
 * Returns the absolute value of [value]
 * @param value The value
 * @return Absolute value of [value]
 */
header fun abs(value: Float): Float

/**
 * Returns the absolute value of [value]
 * @param value The value
 * @return Absolute value of [value]
 */
header fun abs(value: Double): Double
*/

/**
 * Returns the smallest value in [value]
 * @param value The values
 * @return Smallest value in [value]
 */
inline fun min(value: Vector2d): Double {
    return min(value.x, value.y)
}

/**
 * Returns the smaller values between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return [Vector2d] with smaller values between [value1] and [value2]
 */
inline fun min(value1: Vector2d,
               value2: Vector2d): Vector2d {
    return Vector2d(min(value1.x, value2.x),
            min(value1.y, value2.y))
}

/**
 * Returns the smaller values between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return [Vector3d] with smaller values between [value1] and [value2]
 */
inline fun min(value1: Vector3d,
               value2: Vector3d): Vector3d {
    return Vector3d(min(value1.x, value2.x),
            min(value1.y, value2.y),
            min(value1.z, value2.z))
}

/*
/**
 * Returns the smaller value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Smaller value between [value1] and [value2]
 */
header fun min(value1: Int,
               value2: Int): Int

/**
 * Returns the smaller value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Smaller value between [value1] and [value2]
 */
header fun min(value1: Long,
               value2: Long): Long

/**
 * Returns the smaller value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Smaller value between [value1] and [value2]
 */
header fun min(value1: Float,
               value2: Float): Float

/**
 * Returns the smaller value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Smaller value between [value1] and [value2]
 */
header fun min(value1: Double,
               value2: Double): Double
*/

/**
 * Returns the greatest value in [value]
 * @param value The values
 * @return Smallest value in [value]
 */
inline fun max(value: Vector2d): Double {
    return max(value.x, value.y)
}

/**
 * Returns the greatest value in [value]
 * @param value The values
 * @return Smallest value in [value]
 */
inline fun max(value: Vector3d): Double {
    return max(max(value.x, value.y), value.z)
}

/**
 * Returns the greater values between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return [Vector2d] with greater values between [value1] and [value2]
 */
inline fun max(value1: Vector2d,
               value2: Vector2d): Vector2d {
    return Vector2d(max(value1.x, value2.x),
            max(value1.y, value2.y))
}

/**
 * Returns the greater values between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return [Vector3d] with greater values between [value1] and [value2]
 */
inline fun max(value1: Vector3d,
               value2: Vector3d): Vector3d {
    return Vector3d(max(value1.x, value2.x),
            max(value1.y, value2.y),
            max(value1.z, value2.z))
}

/**
 * Returns the greater values between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return [Vector2i] with greater values between [value1] and [value2]
 */
inline fun max(value1: Vector2i,
               value2: Vector2i): Vector2i {
    return Vector2i(max(value1.x, value2.x),
            max(value1.y, value2.y))
}

/**
 * Returns the greater values between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return [Vector3i] with greater values between [value1] and [value2]
 */
inline fun max(value1: Vector3i,
               value2: Vector3i): Vector3i {
    return Vector3i(max(value1.x, value2.x),
            max(value1.y, value2.y),
            max(value1.z, value2.z))
}

/*
/**
 * Returns the greater value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Greater value between [value1] and [value2]
 */
header fun max(value1: Int,
               value2: Int): Int

/**
 * Returns the greater value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Greater value between [value1] and [value2]
 */
header fun max(value1: Long,
               value2: Long): Long

/**
 * Returns the greater value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Greater value between [value1] and [value2]
 */
header fun max(value1: Float,
               value2: Float): Float

/**
 * Returns the greater value between [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return Greater value between [value1] and [value2]
 */
header fun max(value1: Double,
               value2: Double): Double
*/

/**
 * Returns the clamped value of x in [value] between y in [value] and z in
 * [value]
 * @param value The values
 * @return Clamped value of x in [value]
 */
inline fun clamp(value: Vector3d): Double {
    return clamp(value.x, value.y, value.z)
}

/**
 * Returns [value2] if [value1] is less than [value2], [value3] if [value1] is
 * greater than [value3] or otherwise [value2]
 * @param value1 The value
 * @param value2 The minimum value
 * @param value3 The maximum value
 * @return [Vector2d] with [value1] forced into range between [value2] and [value3]
 */
inline fun clamp(value1: Vector2d,
                 value2: Vector2d,
                 value3: Vector2d): Vector2d {
    return Vector2d(clamp(value1.x, value2.x, value3.x),
            clamp(value1.y, value2.y, value3.y))
}

/**
 * Returns [value2] if [value1] is less than [value2], [value3] if [value1] is
 * greater than [value3] or otherwise [value2]
 * @param value1 The value
 * @param value2 The minimum value
 * @param value3 The maximum value
 * @return [Vector3d] with [value1] forced into range between [value2] and [value3]
 */
inline fun clamp(value1: Vector3d,
                 value2: Vector3d,
                 value3: Vector3d): Vector3d {
    return Vector3d(clamp(value1.x, value2.x, value3.x),
            clamp(value1.y, value2.y, value3.y),
            clamp(value1.z, value2.z, value3.z))
}

/**
 * Returns [value2] if [value1] is less than [value2], [value3] if [value1] is
 * greater than [value3] or otherwise [value2]
 * @param value1 The value
 * @param value2 The minimum value
 * @param value3 The maximum value
 * @return [value1] forced into range between [value2] and [value3]
 */
inline fun clamp(value1: Int,
                 value2: Int,
                 value3: Int): Int {
    return max(value2, min(value3, value1))
}

/**
 * Returns [value2] if [value1] is less than [value2], [value3] if [value1] is
 * greater than [value3] or otherwise [value2]
 * @param value1 The value
 * @param value2 The minimum value
 * @param value3 The maximum value
 * @return [value1] forced into range between [value2] and [value3]
 */
inline fun clamp(value1: Long,
                 value2: Long,
                 value3: Long): Long {
    return max(value2, min(value3, value1))
}

/**
 * Returns [value2] if [value1] is less than [value2], [value3] if [value1] is
 * greater than [value3] or otherwise [value2]
 * @param value1 The value
 * @param value2 The minimum value
 * @param value3 The maximum value
 * @return [value1] forced into range between [value2] and [value3]
 */
inline fun clamp(value1: Float,
                 value2: Float,
                 value3: Float): Float {
    return max(value2, min(value3, value1))
}

/**
 * Returns [value2] if [value1] is less than [value2], [value3] if [value1] is
 * greater than [value3] or otherwise [value2]
 * @param value1 The value
 * @param value2 The minimum value
 * @param value3 The maximum value
 * @return [value1] forced into range between [value2] and [value3]
 */
inline fun clamp(value1: Double,
                 value2: Double,
                 value3: Double): Double {
    return max(value2, min(value3, value1))
}

/**
 * Returns [value1] and [value2] mixed together using [ratio]
 *
 * Passing a [ratio] of `0.0` will make it return [value1] and `1.0` [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @param ratio The ratio, should be in range `0.0` to `1.0`
 * @return [value1] and [value2] mixed together
 */
inline fun mix(value1: Float,
               value2: Float,
               ratio: Float): Float {
    return (1.0f - ratio) * value1 + ratio * value2
}

/**
 * Returns [value1] and [value2] mixed together using [ratio]
 *
 * Passing a [ratio] of `0.0` will make it return [value1] and `1.0` [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @param ratio The ratio, should be in range `0.0` to `1.0`
 * @return [value1] and [value2] mixed together
 */
inline fun mix(value1: Double,
               value2: Double,
               ratio: Double): Double {
    return (1.0 - ratio) * value1 + ratio * value2
}

/**
 * Returns the square value of [value]
 * @param value The value
 * @return Square value of [value]
 */
inline fun sqr(value: Int): Int {
    return value * value
}

/**
 * Returns the square value of [value]
 * @param value The value
 * @return Square value of [value]
 */
inline fun sqr(value: Long): Long {
    return value * value
}

/**
 * Returns the square value of [value]
 * @param value The value
 * @return Square value of [value]
 */
inline fun sqr(value: Float): Float {
    return value * value
}

/**
 * Returns the square value of [value]
 * @param value The value
 * @return Square value of [value]
 */
inline fun sqr(value: Double): Double {
    return value * value
}

/**
 * Returns the square value of [value], negative if [value] is negative
 * @param value The value
 * @return Square value of [value], negative if [value] is negative
 */
inline fun sqrNoAbs(value: Int): Int {
    val sqr = sqr(value)
    return if (value < 0) -sqr else sqr
}

/**
 * Returns the square value of [value], negative if [value] is negative
 * @param value The value
 * @return Square value of [value], negative if [value] is negative
 */
inline fun sqrNoAbs(value: Long): Long {
    val sqr = sqr(value)
    return if (value < 0) -sqr else sqr
}

/**
 * Returns the square value of [value], negative if [value] is negative
 * @param value The value
 * @return Square value of [value], negative if [value] is negative
 */
inline fun sqrNoAbs(value: Double): Double {
    val sqr = sqr(value)
    return if (value < 0) -sqr else sqr
}

/**
 * Returns the square value of [value], negative if [value] is negative
 * @param value The value
 * @return Square value of [value], negative if [value] is negative
 */
inline fun sqrNoAbs(value: Float): Float {
    val sqr = sqr(value)
    return if (value < 0) -sqr else sqr
}

/**
 * Returns the square value of [value]
 * @param value The value
 * @return Square value of [value]
 */
inline fun cbe(value: Int): Int {
    return value * value * value
}

/**
 * Returns the cube value of [value]
 * @param value The value
 * @return Cube value of [value]
 */
inline fun cbe(value: Long): Long {
    return value * value * value
}

/**
 * Returns the cube value of [value]
 * @param value The value
 * @return Cube value of [value]
 */
inline fun cbe(value: Float): Float {
    return value * value * value
}

/**
 * Returns the cube value of [value]
 * @param value The value
 * @return Cube value of [value]
 */
inline fun cbe(value: Double): Double {
    return value * value * value
}

/*
/**
 * Returns the square-root value of [value]
 * @param value The value
 * @return Square-root value of [value]
 */
header fun sqrt(value: Float): Float

/**
 * Returns the square-root value of [value]
 * @param value The value
 * @return Square-root value of [value]
 */
header fun sqrt(value: Double): Double

/**
 * Returns the cube-root value of [value]
 * @param value The value
 * @return Cube-root value of [value]
 */
header fun cbrt(value: Float): Float

/**
 * Returns the cube-root value of [value]
 * @param value The value
 * @return Cube-root value of [value]
 */
header fun cbrt(value: Double): Double

/**
 * Returns the next integer below [value]
 * @param value The value
 * @return Next integer below [value]
 */
header fun floor(value: Float): Int

/**
 * Returns the next integer below [value]
 * @param value The value
 * @return Next integer below [value]
 */
header fun floor(value: Double): Int

/**
 * Returns the next integer below [value]
 * @param value The value
 * @return Next integer below [value]
 */
header fun floorL(value: Double): Long

/**
 * Returns the next integer below [value]
 * @param value The value
 * @return Next integer below [value]
 */
header fun floorD(value: Double): Double

/**
 * Returns the nearest integer [value]
 * @param value The value
 * @return Nearest integer below [value]
 */
header fun round(value: Float): Int

/**
 * Returns the nearest integer [value]
 * @param value The value
 * @return Nearest integer below [value]
 */
header fun round(value: Double): Int

/**
 * Returns the nearest integer [value]
 * @param value The value
 * @return Nearest integer below [value]
 */
header fun roundL(value: Double): Long

/**
 * Returns the nearest integer [value]
 * @param value The value
 * @return Nearest integer below [value]
 */
header fun roundD(value: Double): Double

/**
 * Returns the next integer above [value]
 * @param value The value
 * @return Next integer above [value]
 */
header fun ceil(value: Float): Int

/**
 * Returns the next integer above [value]
 * @param value The value
 * @return Next integer above [value]
 */
header fun ceil(value: Double): Int

/**
 * Returns the next integer above [value]
 * @param value The value
 * @return Next integer above [value]
 */
header fun ceilL(value: Double): Long

/**
 * Returns the next integer above [value]
 * @param value The value
 * @return Next integer above [value]
 */
header fun ceilD(value: Double): Double
*/

/**
 * Converts the [Float] from degrees into radians
 * @return Value of the [Float] in radians
 */
inline fun Float.toRad(): Float {
    return toDouble().toRad().toFloat()
}

/**
 * Converts the [Double] from degrees into radians
 * @return Value of the [Double] in radians
 */
inline fun Double.toRad(): Double {
    return this * DEG_2_RAD
}

/**
 * Converts the [Float] from radians into degrees
 * @return Value of the [Float] in degrees
 */
inline fun Float.toDeg(): Float {
    return toDouble().toDeg().toFloat()
}

/**
 * Converts the [Double] from radians into degrees
 * @return Value of the [Double] in degrees
 */
inline fun Double.toDeg(): Double {
    return this * RAD_2_DEG
}

/*
/**
 * Computes the sin of [value]
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
header fun sin(value: Float): Float

/**
 * Computes the sin of [value]
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
header fun sin(value: Double): Double
*/

/**
 * Computes the sin of [value] using a less accurate table
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
inline fun sinTable(value: Float): Float {
    return sinTable(value.toDouble()).toFloat()
}

/**
 * Computes the sin of [value] using a less accurate table
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
inline fun sinTable(value: Double): Double {
    return FastSin.sin(value)
}

/*
/**
 * Computes the asin of [value]
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
header fun asin(value: Float): Float

/**
 * Computes the asin of [value]
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
header fun asin(value: Double): Double
*/

/**
 * Computes the asin of [value] using a less accurate table
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
inline fun asinTable(value: Float): Float {
    return asinTable(value.toDouble()).toFloat()
}

/**
 * Computes the asin of [value] using a less accurate table
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
inline fun asinTable(value: Double): Double {
    return FastAsin.asin(value)
}

/*
/**
 * Computes the cos of [value]
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
header fun cos(value: Float): Float

/**
 * Computes the cos of [value]
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
header fun cos(value: Double): Double
*/

/**
 * Computes the cos of [value] using a less accurate table
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
inline fun cosTable(value: Float): Float {
    return cosTable(value.toDouble()).toFloat()
}

/**
 * Computes the cos of [value] using a less accurate table
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
inline fun cosTable(value: Double): Double {
    return FastSin.cos(value)
}

/*
/**
 * Computes the acos of [value]
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
header fun acos(value: Float): Float

/**
 * Computes the acos of [value]
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
header fun acos(value: Double): Double
*/

/**
 * Computes the acos of [value] using a less accurate table
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
inline fun acosTable(value: Float): Float {
    return acosTable(value.toDouble()).toFloat()
}

/**
 * Computes the acos of [value] using a less accurate table
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
inline fun acosTable(value: Double): Double {
    return FastAsin.acos(value)
}

/*
/**
 * Computes the tan of [value]
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
header fun tan(value: Float): Float

/**
 * Computes the tan of [value]
 * @param value Value in radians
 * @return Result between `-1.0` and `1.0`
 */
header fun tan(value: Double): Double

/**
 * Computes the tanh of [value]
 * @param value The value to use
 * @return Result between `-1.0` and `1.0`
 */
header fun tanh(value: Float): Float

/**
 * Computes the tanh of [value]
 * @param value The value to use
 * @return Result between `-1.0` and `1.0`
 */
header fun tanh(value: Double): Double

/**
 * Computes the atan of [value]
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
header fun atan(value: Float): Float

/**
 * Computes the atan of [value]
 * @param value Value between `-1.0` and `1.0`
 * @return Result between `0.0` and `pi` in radians or `NaN` if an invalid [value] was passed
 */
header fun atan(value: Double): Double

/**
 * Computes the atan2 of [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return The atan2 of [value1] and [value2]
 */
header fun atan2(value1: Float,
                 value2: Float): Float

/**
 * Computes the atan2 of [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return The atan2 of [value1] and [value2]
 */
header fun atan2(value1: Double,
                 value2: Double): Double
*/

/**
 * Computes the atan2 of [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return The atan2 of [value1] and [value2]
 */
inline fun atan2Fast(value1: Float,
                     value2: Float): Float {
    return atan2Fast(value1.toDouble(), value2.toDouble()).toFloat()
}

/**
 * Computes the atan2 of [value1] and [value2]
 * @param value1 The first value
 * @param value2 The second value
 * @return The atan2 of [value1] and [value2]
 */
inline fun atan2Fast(value1: Double,
                     value2: Double): Double {
    return FastAtan2.atan2(value1, value2)
}

/*
/**
 * Computes [value1] to the power of [value2]
 * @param value1 The base value
 * @param value2 The exponent
 * @return [value1] to the power of [value2]
 */
header fun pow(value1: Float,
               value2: Float): Float

/**
 * Computes [value1] to the power of [value2]
 * @param value1 The base value
 * @param value2 The exponent
 * @return [value1] to the power of [value2]
 */
header fun pow(value1: Double,
               value2: Double): Double
*/

/**
 * Computes the difference between the angles [value1] and [value2]
 * @param value1 The first value in degrees
 * @param value2 The second value in degrees
 * @return Returns the difference in range `-180.0` and `180.0`
 */
inline fun angleDiff(value1: Float,
                     value2: Float): Float {
    return angleDiff(value1.toDouble(), value2.toDouble()).toFloat()
}

/**
 * Computes the difference between the angles [value1] and [value2]
 * @param value1 The first value in degrees
 * @param value2 The second value in degrees
 * @return Returns the difference in range `-180.0` and `180.0`
 */
inline fun angleDiff(value1: Double,
                     value2: Double): Double {
    return diff(value1, value2, 360.0)
}

/**
 * Computes the difference between [value1] and [value2] with the assumption
 * that they wrap around at [modulus], useful for computing differences of
 * angles
 * @param value1 The first value
 * @param value2 The second value
 * @param modulus The modulus to use
 * @return Returns the difference in range `-[modulus] / 2` and `[modulus] / 2`
 */
inline fun diff(value1: Float,
                value2: Float,
                modulus: Float): Float {
    return diff(value1.toDouble(), value2.toDouble(),
            modulus.toDouble()).toFloat()
}

/**
 * Computes the difference between [value1] and [value2] with the assumption
 * that they wrap around at [modulus], useful for computing differences of
 * angles
 * @param value1 The first value
 * @param value2 The second value
 * @param modulus The modulus to use
 * @return Returns the difference in range `-[modulus] / 2` and `[modulus] / 2`
 */
inline fun diff(value1: Double,
                value2: Double,
                modulus: Double): Double {
    return FastMath.diff(value1, value2, modulus)
}

/**
 * Moves values between `0.0` and `1.0` into range `margin` and
 * `1.0 - margin` by scaling and offsetting them
 * @param value The value to transform
 * @param margin The margin on the sides
 * @return Returns the value after the linear transformation
 */
inline fun margin(value: Float,
                  margin: Float): Float {
    return margin + value * (1.0f - margin * 2.0f)
}

/**
 * Moves values between `0.0` and `1.0` into range `margin` and
 * `1.0 - margin` by scaling and offsetting them
 * @param value The value to transform
 * @param margin The margin on the sides
 * @return Returns the value after the linear transformation
 */
inline fun margin(value: Double,
                  margin: Double): Double {
    return margin + value * (1.0 - margin * 2.0)
}

/**
 * Computes the next higher power of two for the given value
 * @param value The value
 * @return Returns the smallest higher power of two greater or equal to value
 */
inline fun nextPowerOfTwo(value: Int): Int {
    return FastMath.nextPowerOfTwo(value)
}

/*
/**
 * Computes the logarithm with base 2 of [value]
 * @param value The value
 * @return Returns the logarithm with base 2 of [value]
 */
header fun lb(value: Int): Int
*/

/**
 * Computes the modulus of the given number and [value]
 * Unlike the normal modulo operator this always returns a positive value
 * @param value The divisor
 * @receiver The dividend
 * @return Returns the modulus of the given number and [value]
 */
inline infix fun Float.remP(value: Float): Float {
    val mod = this % value
    return if (mod < 0.0f) {
        mod + value
    } else {
        mod
    }
}

/**
 * Computes the modulus of the given number and [value]
 * Unlike the normal modulo operator this always returns a positive value
 * @param value The divisor
 * @receiver The dividend
 * @return Returns the modulus of the given number and [value]
 */
inline infix fun Double.remP(value: Double): Double {
    val mod = this % value
    return if (mod < 0.0) {
        mod + value
    } else {
        mod
    }
}

/**
 * Computes the modulus of the given number and [value]
 * Unlike the normal modulo operator this always returns a positive value
 * @param value The divisor
 * @receiver The dividend
 * @return Returns the modulus of the given number and [value]
 */
inline infix fun Int.remP(value: Int): Int {
    val mod = this % value
    return if (mod < 0) {
        mod + value
    } else {
        mod
    }
}

/**
 * Computes the modulus of the given number and [value]
 * Unlike the normal modulo operator this always returns a positive value
 * @param value The divisor
 * @receiver The dividend
 * @return Returns the modulus of the given number and [value]
 */
inline infix fun Long.remP(value: Long): Long {
    val mod = this % value
    return if (mod < 0L) {
        mod + value
    } else {
        mod
    }
}
