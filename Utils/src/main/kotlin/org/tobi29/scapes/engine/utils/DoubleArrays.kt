// GENERATED FILE, DO NOT EDIT DIRECTLY!!!
// Generation script can be found in `resources/codegen/GenArrays.kts`.
// Run `resources/codegen/codegen.sh` to update sources.

@file:Suppress("NOTHING_TO_INLINE")

package org.tobi29.scapes.engine.utils

/**
 * Read-only slice of an array, indexed in elements
 */
interface DoubleArraySliceRO : ArrayVarSlice<Double> {
    override fun slice(index: Int): DoubleArraySliceRO

    override fun slice(index: Int,
                       size: Int): DoubleArraySliceRO

    /**
     * Returns the element at the given index in the slice
     * @param index Index of the element
     * @return The value at the given index
     */
    operator fun get(index: Int): Double

    fun getDouble(index: Int): Double = get(index)

    fun getDoubles(index: Int,
                   slice: DoubleArraySlice) {
        var j = index
        for (i in 0 until slice.size) {
            slice.set(i, get(j++))
        }
    }

    override fun iterator(): Iterator<Double> =
            object : SliceIterator<Double>(size) {
                override fun access(index: Int) = get(index)
            }
}

/**
 * Slice of an array, indexed in elements
 */
interface DoubleArraySlice : DoubleArraySliceRO {
    override fun slice(index: Int): DoubleArraySlice

    override fun slice(index: Int,
                       size: Int): DoubleArraySlice

    /**
     * Sets the element at the given index in the slice
     * @param index Index of the element
     * @param value The value to set to
     */
    operator fun set(index: Int,
                     value: Double)

    fun setDouble(index: Int,
                  value: Double) = set(index, value)

    fun setDoubles(index: Int,
                   slice: DoubleArraySliceRO) =
            slice.getDoubles(0, slice(index, slice.size))
}

/**
 * Slice of a normal heap array
 */
open class HeapDoubleArraySlice(
        val array: DoubleArray,
        override final val offset: Int,
        override final val size: Int
) : HeapArrayVarSlice<Double>, DoubleArraySlice {
    override fun slice(index: Int): HeapDoubleArraySlice =
            slice(index, size - index)

    override fun slice(index: Int,
                       size: Int): HeapDoubleArraySlice =
            prepareSlice(index, size, array,
                    ::HeapDoubleArraySlice)

    override final fun get(index: Int): Double = array[index(offset, size,
            index)]

    override final fun set(index: Int,
                           value: Double) = array.set(
            index(offset, size, index), value)

    override final fun getDoubles(index: Int,
                                  slice: DoubleArraySlice) {
        if (slice !is HeapDoubleArraySlice) return super.getDoubles(index,
                slice)

        if (index < 0 || index + slice.size > size)
            throw IndexOutOfBoundsException("Invalid index or view too long")

        copy(array, slice.array, slice.size, index + this.offset, slice.offset)
    }

    override final fun setDoubles(index: Int,
                                  slice: DoubleArraySliceRO) {
        if (slice !is HeapDoubleArraySlice) return super.setDoubles(index,
                slice)

        if (index < 0 || index + slice.size > size)
            throw IndexOutOfBoundsException("Invalid index or view too long")

        copy(slice.array, array, slice.size, slice.offset, index + this.offset)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DoubleArraySliceRO) return false
        for (i in 0 until size) {
            if (this[i] != other[i]) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var h = 1
        for (i in 0 until size) {
            h = h * 31 + (this[i]?.hashCode() ?: 0)
        }
        return h
    }
}

/**
 * Creates a slice from the given array, which holds the array itself as backing
 * storage
 * @param index Index to start the slice at
 * @param size Amount of elements in slice
 * @receiver The array to create a slice of
 * @return A slice from the given array
 */
inline fun DoubleArray.sliceOver(
        index: Int = 0,
        size: Int = this.size - index
): HeapDoubleArraySlice = HeapDoubleArraySlice(this, index, size)

/**
 * Class wrapping an array to provide nicer support for 2-dimensional data.
 *
 * The layout for the dimensions is as follows:
 * `index = y * width + x`
 */
class DoubleArray2
/**
 * Creates a new wrapper around the given array.
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @param array Array for storing elements
 */
(
        /**
         * Width of the wrapper.
         */
        val width: Int,
        /**
         * Height of the wrapper.
         */
        val height: Int,
        private val array: DoubleArray) : Iterable<Double> {
    /**
     * Size of the array.
     */
    val size = width * height

    init {
        if (size != array.size) {
            throw IllegalArgumentException(
                    "Array has invalid size: ${array.size} (should be $size)")
        }
    }

    /**
     * Retrieve an element from the array.
     * @param x Index on the first axis
     * @param y Index on the second axis
     * @throws IndexOutOfBoundsException When accessing indices out of bounds
     * @return The element at the given position
     */
    operator fun get(x: Int,
                     y: Int): Double {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw IndexOutOfBoundsException("$x $y")
        }
        return array[y * width + x]
    }

    /**
     * Retrieve an element from the array or `null` if out of bounds.
     * @param x Index on the first axis
     * @param y Index on the second axis
     * @return The element at the given position or `null` if out of bounds
     */
    fun getOrNull(x: Int,
                  y: Int): Double? {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return null
        }
        return array[y * width + x]
    }

    /**
     * Stores an element in the array.
     * @param x Index on the first axis
     * @param y Index on the second axis
     * @param value The element to store
     */
    operator fun set(x: Int,
                     y: Int,
                     value: Double) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw IndexOutOfBoundsException("$x $y")
        }
        array[y * width + x] = value
    }

    /**
     * Creates an iterator for iterating over the elements of the array.
     */
    override operator fun iterator() = array.iterator()

    /**
     * Makes a shallow copy of the array and wrapper.
     * @return A new wrapper around a new array
     */
    fun copyOf() = DoubleArray2(width, height, array.copyOf())
}

/**
 * Calls the given [block] with all indices of the given wrapper ordered by
 * their layout in the array.
 * @receiver The wrapper to iterate through
 * @param block Called with x and y coords of the element
 */
inline fun DoubleArray2.indices(block: (Int, Int) -> Unit) {
    for (y in 0 until height) {
        for (x in 0 until width) {
            block(x, y)
        }
    }
}

/**
 * Creates a new array and makes it accessible using a wrapper
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @param init Returns values to be inserted by default
 * @return Wrapper around a new array
 */
inline fun DoubleArray2(width: Int,
                        height: Int,
                        init: (Int, Int) -> Double) =
        DoubleArray2(width, height) { i ->
            val x = i % width
            val y = i / width
            init(x, y)
        }

/**
 * Creates a new array and makes it accessible using a wrapper
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @param init Returns values to be inserted by default
 * @return Wrapper around a new array
 */
inline fun DoubleArray2(width: Int,
                        height: Int,
                        init: (Int) -> Double) =
        DoubleArray2(width, height, DoubleArray(width * height) { init(it) })

/**
 * Class wrapping an array to provide nicer support for 3-dimensional data.
 *
 * The layout for the dimensions is as follows:
 * `index = (z * height + y) * width + x`
 */
class DoubleArray3
/**
 * Creates a new wrapper around the given array.
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @param depth Depth of the wrapper
 * @param array Array for storing elements
 */
(
        /**
         * Width of the wrapper.
         */
        val width: Int,
        /**
         * Height of the wrapper.
         */
        val height: Int,
        /**
         * Depth of the wrapper.
         */
        val depth: Int,
        private val array: DoubleArray) : Iterable<Double> {
    /**
     * Size of the array.
     */
    val size = width * height * depth

    init {
        if (size != array.size) {
            throw IllegalArgumentException(
                    "Array has invalid size: ${array.size} (should be $size)")
        }
    }

    /**
     * Retrieve an element from the array.
     * @param x Index on the first axis
     * @param y Index on the second axis
     * @param z Index on the third axis
     * @throws IndexOutOfBoundsException When accessing indices out of bounds
     * @return The element at the given position
     */
    operator fun get(x: Int,
                     y: Int,
                     z: Int): Double {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
            throw IndexOutOfBoundsException("$x $y $z")
        }
        return array[(z * height + y) * width + x]
    }

    /**
     * Retrieve an element from the array or `null` if out of bounds.
     * @param x Index on the first axis
     * @param y Index on the second axis
     * @param z Index on the third axis
     * @return The element at the given position or `null` if out of bounds
     */
    fun getOrNull(x: Int,
                  y: Int,
                  z: Int): Double? {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
            return null
        }
        return array[(z * height + y) * width + x]
    }

    /**
     * Stores an element in the array.
     * @param x Index on the first axis
     * @param y Index on the second axis
     * @param z Index on the third axis
     * @param value The element to store
     */
    operator fun set(x: Int,
                     y: Int,
                     z: Int,
                     value: Double) {
        if (x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
            throw IndexOutOfBoundsException("$x $y $z")
        }
        array[(z * height + y) * width + x] = value
    }

    /**
     * Creates an iterator for iterating over the elements of the array.
     */
    override operator fun iterator() = array.iterator()

    /**
     * Makes a shallow copy of the array and wrapper.
     * @return A new wrapper around a new array
     */
    fun copyOf() = DoubleArray3(width, height, depth, array.copyOf())
}

/**
 * Calls the given [block] with all indices of the given wrapper ordered by
 * their layout in the array.
 * @receiver The wrapper to iterate through
 * @param block Called with x, y and z coords of the element
 */
inline fun DoubleArray3.indices(block: (Int, Int, Int) -> Unit) {
    for (z in 0 until depth) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                block(x, y, z)
            }
        }
    }
}

/**
 * Creates a new array and makes it accessible using a wrapper
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @param depth Depth of the wrapper
 * @param init Returns values to be inserted by default
 * @return Wrapper around a new array
 */
inline fun DoubleArray3(width: Int,
                        height: Int,
                        depth: Int,
                        init: (Int, Int, Int) -> Double) =
        DoubleArray3(width, height, depth) { i ->
            val x = i % width
            val j = i / width
            val y = j % height
            val z = j / height
            init(x, y, z)
        }

/**
 * Creates a new array and makes it accessible using a wrapper
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @param depth Depth of the wrapper
 * @param init Returns values to be inserted by default
 * @return Wrapper around a new array
 */
inline fun DoubleArray3(width: Int,
                        height: Int,
                        depth: Int,
                        init: (Int) -> Double) =
        DoubleArray3(width, height, depth,
                DoubleArray(width * height * depth) { init(it) })

/**
 * Fills the given array with values
 * @receiver Array to fill
 * @param supplier Supplier called for each value written to the array
 */
inline fun DoubleArray.fill(supplier: (Int) -> Double) {
    for (i in indices) {
        set(i, supplier(i))
    }
}

/**
 * Calls the given [block] with all indices of the given wrapper ordered by
 * their layout in the array and stores its return value in the array.
 * @receiver The wrapper to iterate through
 * @param block Called with x and y coords of the element
 */
inline fun DoubleArray2.fill(block: (Int, Int) -> Double) = indices { x, y ->
    this[x, y] = block(x, y)
}

/**
 * Calls the given [block] with all indices of the given wrapper ordered by
 * their layout in the array and stores its return value in the array.
 * @receiver The wrapper to iterate through
 * @param block Called with x, y and z coords of the element
 */
inline fun DoubleArray3.fill(block: (Int, Int, Int) -> Double) = indices { x, y, z ->
    this[x, y, z] = block(x, y, z)
}

/**
 * Copy data from the [src] array to [dest]
 * @param src The array to copy from
 * @param dest The array to copy to
 * @param length The amount of elements to copy
 * @param offsetSrc Offset in the source array
 * @param offsetDest Offset in the destination array
 */
inline fun copy(src: DoubleArray,
                dest: DoubleArray,
                length: Int = src.size.coerceAtMost(dest.size),
                offsetSrc: Int = 0,
                offsetDest: Int = 0) =
        copyArray(src, dest, length, offsetSrc, offsetDest)

/**
 * Creates a new array and makes it accessible using a wrapper
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @return Wrapper around a new array
 */
inline fun DoubleArray2(width: Int,
                        height: Int) =
        DoubleArray2(width, height, DoubleArray(width * height))

/**
 * Creates a new array and makes it accessible using a wrapper
 * @param width Width of the wrapper
 * @param height Height of the wrapper
 * @param depth Depth of the wrapper
 * @return Wrapper around a new array
 */
inline fun DoubleArray3(width: Int,
                        height: Int,
                        depth: Int) =
        DoubleArray3(width, height, depth, DoubleArray(width * height * depth))
