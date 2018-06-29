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

package org.tobi29.io.compression.deflate

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.tobi29.arrays.readAsByteArray
import org.tobi29.assertions.byteArrays
import org.tobi29.assertions.shouldEqual
import org.tobi29.io.*
import java.util.zip.DeflaterOutputStream

object DeflateTests : Spek({
    fun deflateJvm(
        input: ReadableByteStream,
        level: Int = -1
    ) = MemoryViewStreamDefault().also { stream ->
        DeflaterOutputStream(ByteStreamOutputStream(stream)).use { streamOut ->
            input.process {
                it.readAsByteArray { array, index, offset ->
                    streamOut.write(array, index, offset)
                }
            }
        }
        stream.flip()
    }.bufferSlice()

    given("any byte array") {
        val arrays by memoized { byteArrays(32, 8) }
        on("compressing and decompressing") {
            for (array in arrays) {
                val data = array.viewBE
                val compressedJvm = deflateJvm(MemoryViewReadableStream(data))
                val compressed = deflate(MemoryViewReadableStream(data)).viewBE

                it("should give the same compressed data as zlib") {
                    compressed shouldEqual compressedJvm
                }

                val decompressed = inflate(MemoryViewReadableStream(compressed))

                it("should result with the same array") {
                    decompressed shouldEqual data
                }
            }
        }
    }
})
