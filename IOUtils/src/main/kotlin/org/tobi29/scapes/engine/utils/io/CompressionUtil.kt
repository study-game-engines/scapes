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

package org.tobi29.scapes.engine.utils.io

/**
 * Utility class for compressing and decompressing data
 */
object CompressionUtil {

    // TODO: @Throws(IOException::class)
    fun compress(input: ReadableByteStream,
                 level: Int = -1,
                 bufferProvider: ByteBufferProvider = DefaultByteBufferProvider,
                 growth: (Int) -> Int = { it + 8192 }): ByteBuffer {
        val stream = ByteBufferStream(bufferProvider, growth)
        compress(input, stream, level)
        return stream.buffer()
    }

    // TODO: @Throws(IOException::class)
    fun compress(input: ReadableByteStream,
                 output: WritableByteStream,
                 level: Int = 1) {
        ZDeflater(level).use { filter -> filter(input, output, filter) }
    }

    // TODO: @Throws(IOException::class)
    fun decompress(input: ReadableByteStream,
                   bufferProvider: ByteBufferProvider = DefaultByteBufferProvider,
                   growth: (Int) -> Int = { it + 8192 }): ByteBuffer {
        val output = ByteBufferStream(bufferProvider, growth)
        decompress(input, output)
        return output.buffer()
    }

    // TODO: @Throws(IOException::class)
    fun decompress(input: ReadableByteStream,
                   output: WritableByteStream) {
        ZInflater().use { filter -> filter(input, output, filter) }
    }

    // TODO: @Throws(IOException::class)
    fun filter(input: ReadableByteStream,
               output: WritableByteStream,
               filter: Filter) {
        while (filter.input(input)) {
            while (!filter.needsInput()) {
                val len = filter.output(output)
                if (len <= 0) {
                    break
                }
            }
        }
        filter.finish()
        while (!filter.finished()) {
            val len = filter.output(output)
            if (len <= 0) {
                break
            }
        }
        filter.reset()
    }

    interface Filter : AutoCloseable {
        // TODO: @Throws(IOException::class)
        fun input(buffer: ReadableByteStream): Boolean

        // TODO: @Throws(IOException::class)
        fun output(buffer: WritableByteStream): Int

        fun finish()

        fun needsInput(): Boolean

        fun finished(): Boolean

        fun reset()

        override fun close()
    }
}
