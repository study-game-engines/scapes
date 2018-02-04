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

package org.tobi29.math.vector

import org.tobi29.arrays.Ints
import org.tobi29.io.tag.ReadTagMutableMap
import org.tobi29.io.tag.toInt

data class MutableVector2i(
    var x: Int = 0,
    var y: Int = 0
) : Ints {
    constructor(vector: Vector2i) : this(vector.x, vector.y)

    constructor(vector: MutableVector2i) : this(vector.x, vector.y)

    override val size: Int get() = 2

    fun now(): Vector2i = Vector2i(x, y)

    override fun get(index: Int): Int = when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("$index")
    }

    override fun set(
        index: Int,
        value: Int
    ): Unit = when (index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("$index")
    }

    fun setX(x: Int) = apply {
        this.x = x
    }

    fun setY(y: Int) = apply {
        this.y = y
    }

    fun setXY(
        x: Int,
        y: Int
    ) = apply {
        this.x = x
        this.y = y
    }

    fun set(a: Vector2i) = apply {
        x = a.x
        y = a.y
    }

    fun set(a: MutableVector2i) = apply {
        x = a.x
        y = a.y
    }

    fun negate() = apply {
        x = -x
        y = -y
    }

    fun add(a: Int) = apply {
        x += a
        y += a
    }

    fun addX(x: Int) = apply {
        this.x += x
    }

    fun addY(y: Int) = apply {
        this.y += y
    }

    fun add(vector: Vector2i) = apply {
        x += vector.x
        y += vector.y
    }

    fun add(vector: MutableVector2i) = apply {
        x += vector.x
        y += vector.y
    }

    fun subtract(a: Int) = apply {
        x -= a
        y -= a
    }

    fun subtract(vector: Vector2i) = apply {
        x -= vector.x
        y -= vector.y
    }

    fun subtract(vector: MutableVector2i) = apply {
        x -= vector.x
        y -= vector.y
    }

    fun multiply(a: Int) = apply {
        x *= a
        y *= a
    }

    fun multiply(vector: Vector2i) = apply {
        x *= vector.x
        y *= vector.y
    }

    fun multiply(vector: MutableVector2i) = apply {
        x *= vector.x
        y *= vector.y
    }

    fun divide(a: Int) = apply {
        x /= a
        y /= a
    }

    fun divide(vector: Vector2i) = apply {
        x /= vector.x
        y /= vector.y
    }

    fun divide(vector: MutableVector2i) = apply {
        x /= vector.x
        y /= vector.y
    }

    fun set(map: ReadTagMutableMap) {
        map["X"]?.toInt()?.let { x = it }
        map["Y"]?.toInt()?.let { y = it }
    }
}
