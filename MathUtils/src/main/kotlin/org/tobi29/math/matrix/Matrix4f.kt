/*
 * Copyright 2012-2018 Tobi29
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

package org.tobi29.math.matrix

import org.tobi29.arrays.FloatArray2
import org.tobi29.math.cosTable
import org.tobi29.math.sinTable
import org.tobi29.math.vector.Vector3d
import org.tobi29.math.vector.dot
import org.tobi29.stdex.copy
import org.tobi29.stdex.math.toRad
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Matrix4f {
    val values = FloatArray(16)
    private val values2 = FloatArray2(4, 4, values)

    operator fun get(
        x: Int,
        y: Int
    ) = values2[y, x]

    operator fun set(
        x: Int,
        y: Int,
        value: Float
    ) {
        values2[y, x] = value
    }

    fun set(matrix: Matrix4f) {
        copy(matrix.values, values)
    }

    fun identity() = set(
        1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
    )

    fun set(
        xx: Float,
        xy: Float,
        xz: Float,
        xw: Float,
        yx: Float,
        yy: Float,
        yz: Float,
        yw: Float,
        zx: Float,
        zy: Float,
        zz: Float,
        zw: Float,
        wx: Float,
        wy: Float,
        wz: Float,
        ww: Float
    ) {
        values[0] = xx
        values[1] = yx
        values[2] = zx
        values[3] = wx
        values[4] = xy
        values[5] = yy
        values[6] = zy
        values[7] = wy
        values[8] = xz
        values[9] = yz
        values[10] = zz
        values[11] = wz
        values[12] = xw
        values[13] = yw
        values[14] = zw
        values[15] = ww
    }

    fun scale(
        x: Float,
        y: Float,
        z: Float
    ) {
        for (i in 0..3) {
            values[i] = values[i] * x
        }
        for (i in 4..7) {
            values[i] = values[i] * y
        }
        for (i in 8..11) {
            values[i] = values[i] * z
        }
    }

    fun translate(
        x: Float,
        y: Float,
        z: Float
    ) {
        wx += xx * x + yx * y + zx * z
        wy += xy * x + yy * y + zy * z
        wz += xz * x + yz * y + zz * z
        ww += xw * x + yw * y + zw * z
    }

    fun rotate(
        angle: Float,
        x: Float,
        y: Float,
        z: Float
    ) {
        rotateRad(angle.toRad(), x, y, z)
    }

    fun rotateRad(
        angle: Float,
        x: Float,
        y: Float,
        z: Float
    ) {
        val cos = cosTable(angle)
        val sin = sinTable(angle)
        rotate(cos, sin, x, y, z)
    }

    fun rotateAccurate(
        angle: Double,
        x: Float,
        y: Float,
        z: Float
    ) {
        rotateAccurateRad(angle.toRad(), x, y, z)
    }

    fun rotateAccurateRad(
        angle: Double,
        x: Float,
        y: Float,
        z: Float
    ) {
        val cos = cos(angle).toFloat()
        val sin = sin(angle).toFloat()
        rotate(cos, sin, x, y, z)
    }

    private fun rotate(
        cos: Float,
        sin: Float,
        x: Float,
        y: Float,
        z: Float
    ) {
        val oneMinusCos = 1.0f - cos
        val mxy = x * y
        val myz = y * z
        val mxz = x * z
        val xSin = x * sin
        val ySin = y * sin
        val zSin = z * sin
        val f00 = x * x * oneMinusCos + cos
        val f01 = mxy * oneMinusCos + zSin
        val f02 = mxz * oneMinusCos - ySin
        val f10 = mxy * oneMinusCos - zSin
        val f11 = y * y * oneMinusCos + cos
        val f12 = myz * oneMinusCos + xSin
        val f20 = mxz * oneMinusCos + ySin
        val f21 = myz * oneMinusCos - xSin
        val f22 = z * z * oneMinusCos + cos
        val v00 = xx
        val v01 = xy
        val v02 = xz
        val v03 = xw
        val v10 = yx
        val v11 = yy
        val v12 = yz
        val v13 = yw
        val v20 = zx
        val v21 = zy
        val v22 = zz
        val v23 = zw
        val t00 = v00 * f00 + v10 * f01 + v20 * f02
        val t01 = v01 * f00 + v11 * f01 + v21 * f02
        val t02 = v02 * f00 + v12 * f01 + v22 * f02
        val t03 = v03 * f00 + v13 * f01 + v23 * f02
        val t10 = v00 * f10 + v10 * f11 + v20 * f12
        val t11 = v01 * f10 + v11 * f11 + v21 * f12
        val t12 = v02 * f10 + v12 * f11 + v22 * f12
        val t13 = v03 * f10 + v13 * f11 + v23 * f12
        zx = v00 * f20 + v10 * f21 + v20 * f22
        zy = v01 * f20 + v11 * f21 + v21 * f22
        zz = v02 * f20 + v12 * f21 + v22 * f22
        zw = v03 * f20 + v13 * f21 + v23 * f22
        xx = t00
        xy = t01
        xz = t02
        xw = t03
        yx = t10
        yy = t11
        yz = t12
        yw = t13
    }

    fun multiply(
        o: Matrix4f,
        d: Matrix4f
    ) {
        val v00 = xx
        val v01 = xy
        val v02 = xz
        val v03 = xw
        val v10 = yx
        val v11 = yy
        val v12 = yz
        val v13 = yw
        val v20 = zx
        val v21 = zy
        val v22 = zz
        val v23 = zw
        val v30 = wx
        val v31 = wy
        val v32 = wz
        val v33 = ww
        val o00 = o.xx
        val o01 = o.xy
        val o02 = o.xz
        val o03 = o.xw
        val o10 = o.yx
        val o11 = o.yy
        val o12 = o.yz
        val o13 = o.yw
        val o20 = o.zx
        val o21 = o.zy
        val o22 = o.zz
        val o23 = o.zw
        val o30 = o.wx
        val o31 = o.wy
        val o32 = o.wz
        val o33 = o.ww
        d.xx = v00 * o00 + v10 * o01 + v20 * o02 + v30 * o03
        d.xy = v01 * o00 + v11 * o01 + v21 * o02 + v31 * o03
        d.xz = v02 * o00 + v12 * o01 + v22 * o02 + v32 * o03
        d.xw = v03 * o00 + v13 * o01 + v23 * o02 + v33 * o03
        d.yx = v00 * o10 + v10 * o11 + v20 * o12 + v30 * o13
        d.yy = v01 * o10 + v11 * o11 + v21 * o12 + v31 * o13
        d.yz = v02 * o10 + v12 * o11 + v22 * o12 + v32 * o13
        d.yw = v03 * o10 + v13 * o11 + v23 * o12 + v33 * o13
        d.zx = v00 * o20 + v10 * o21 + v20 * o22 + v30 * o23
        d.zy = v01 * o20 + v11 * o21 + v21 * o22 + v31 * o23
        d.zz = v02 * o20 + v12 * o21 + v22 * o22 + v32 * o23
        d.zw = v03 * o20 + v13 * o21 + v23 * o22 + v33 * o23
        d.wx = v00 * o30 + v10 * o31 + v20 * o32 + v30 * o33
        d.wy = v01 * o30 + v11 * o31 + v21 * o32 + v31 * o33
        d.wz = v02 * o30 + v12 * o31 + v22 * o32 + v32 * o33
        d.ww = v03 * o30 + v13 * o31 + v23 * o32 + v33 * o33
    }

    fun multiply(v: Vector3d): Vector3d =
        matrix4dMultiply(
            xx.toDouble(), xy.toDouble(), xz.toDouble(),
            xw.toDouble(), yx.toDouble(), yy.toDouble(), yz.toDouble(),
            yw.toDouble(), zx.toDouble(), zy.toDouble(), zz.toDouble(),
            zw.toDouble(), wx.toDouble(), wy.toDouble(), wz.toDouble(),
            ww.toDouble(), v.x, v.y, v.z, 1.0
        ) { x, y, z, _ ->
            Vector3d(x, y, z)
        }

    fun perspective(
        fov: Float,
        aspectRatio: Float,
        near: Float,
        far: Float
    ) {
        val delta = far - near
        val cotangent = 1.0f / tan((fov / 2.0f).toRad())
        xx = cotangent / aspectRatio
        yy = cotangent
        val value2 = -(far + near) / delta
        zz = value2
        val value1 = -1.0f
        zw = value1
        val value = -2.0f * near * far / delta
        wz = value
        ww = 0.0f
    }

    fun orthogonal(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        zNear: Float = -1024.0f,
        zFar: Float = 1024.0f
    ) {
        val right = x + width
        val bottom = y + height

        xx = 2.0f / (right - x)
        xy = 0.0f
        xz = 0.0f
        xw = 0.0f
        yx = 0.0f
        yy = 2.0f / (y - bottom)
        yz = 0.0f
        yw = 0.0f
        zx = 0.0f
        zy = 0.0f
        zz = 2.0f / (zFar - zNear)
        zw = 0.0f
        val value2 = -(right + x) / (right - x)
        wx = value2
        val value1 = -(y + bottom) / (y - bottom)
        wy = value1
        val value = -(zFar + zNear) / (zFar - zNear)
        wz = value
        ww = 1.0f
    }

    fun invert(
        temp: Matrix4f,
        out: Matrix4f
    ): Boolean {
        if (temp !== this) {
            temp.set(this)
        }
        out.identity()
        for (i in 0..3) {
            val i4 = i shl 2
            var swap = i
            for (j in i + 1..3) {
                if (abs(temp.values[(j shl 2) + i]) > abs(
                        temp.values[i4 + i]
                    )) {
                    swap = j
                }
            }
            if (swap != i) {
                val swap4 = swap shl 2
                for (k in 0..3) {
                    var t = temp.values[i4 + k]
                    temp.values[i4 + k] = temp.values[swap4 + k]
                    temp.values[swap4 + k] = t
                    t = out.values[i4 + k]
                    out.values[i4 + k] = out.values[swap4 + k]
                    out.values[swap4 + k] = t
                }
            }
            if (temp.values[i4 + i] == 0f) {
                return false
            }
            var t = temp.values[i4 + i]
            for (k in 0..3) {
                temp.values[i4 + k] = temp.values[i4 + k] / t
                out.values[i4 + k] = out.values[i4 + k] / t
            }
            for (j in 0..3) {
                if (j != i) {
                    val j4 = j shl 2
                    t = temp.values[j4 + i]
                    for (k in 0..3) {
                        temp.values[j4 + k] = temp.values[j4 + k] - temp.values[i4 + k] *
                                t
                        out.values[j4 + k] = out.values[j4 + k] - out.values[i4 + k] *
                                t
                    }
                }
            }
        }
        return true
    }
}

inline fun <R> matrix4fMultiply(
    xx: Float, xy: Float, xz: Float, xw: Float,
    yx: Float, yy: Float, yz: Float, yw: Float,
    zx: Float, zy: Float, zz: Float, zw: Float,
    wx: Float, wy: Float, wz: Float, ww: Float,
    x: Float, y: Float, z: Float, w: Float,
    output: (Float, Float, Float, Float) -> R
): R = output(
    dot(xx, yx, zx, wx, x, y, z, w),
    dot(xy, yy, zy, wy, x, y, z, w),
    dot(xz, yz, zz, wz, x, y, z, w),
    dot(xw, yw, zw, ww, x, y, z, w)
)

inline var Matrix4f.xx: Float
    get() = get(0, 0)
    set(value) = set(0, 0, value)
inline var Matrix4f.yx: Float
    get() = get(1, 0)
    set(value) = set(1, 0, value)
inline var Matrix4f.zx: Float
    get() = get(2, 0)
    set(value) = set(2, 0, value)
inline var Matrix4f.wx: Float
    get() = get(3, 0)
    set(value) = set(3, 0, value)
inline var Matrix4f.xy: Float
    get() = get(0, 1)
    set(value) = set(0, 1, value)
inline var Matrix4f.yy: Float
    get() = get(1, 1)
    set(value) = set(1, 1, value)
inline var Matrix4f.zy: Float
    get() = get(2, 1)
    set(value) = set(2, 1, value)
inline var Matrix4f.wy: Float
    get() = get(3, 1)
    set(value) = set(3, 1, value)
inline var Matrix4f.xz: Float
    get() = get(0, 2)
    set(value) = set(0, 2, value)
inline var Matrix4f.yz: Float
    get() = get(1, 2)
    set(value) = set(1, 2, value)
inline var Matrix4f.zz: Float
    get() = get(2, 2)
    set(value) = set(2, 2, value)
inline var Matrix4f.wz: Float
    get() = get(3, 2)
    set(value) = set(3, 2, value)
inline var Matrix4f.xw: Float
    get() = get(0, 3)
    set(value) = set(0, 3, value)
inline var Matrix4f.yw: Float
    get() = get(1, 3)
    set(value) = set(1, 3, value)
inline var Matrix4f.zw: Float
    get() = get(2, 3)
    set(value) = set(2, 3, value)
inline var Matrix4f.ww: Float
    get() = get(3, 3)
    set(value) = set(3, 3, value)
