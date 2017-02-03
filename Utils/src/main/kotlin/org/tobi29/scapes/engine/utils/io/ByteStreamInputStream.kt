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

import org.tobi29.scapes.engine.utils.ByteBuffer
import java.io.IOException
import java.io.InputStream

class ByteStreamInputStream(private val stream: ReadableByteStream) : InputStream() {
    private val single = ByteBuffer(1)

    @Throws(IOException::class)
    override fun read(): Int {
        single.clear()
        stream.getSome(single)
        single.flip()
        if (!single.hasRemaining()) {
            return -1
        }
        return single.get().toInt()
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray,
                      off: Int,
                      len: Int): Int {
        return stream.getSome(b, off, len)
    }

    @Throws(IOException::class)
    override fun skip(n: Long): Long {
        val len = n.toInt()
        stream.skip(len)
        return len.toLong()
    }

    @Throws(IOException::class)
    override fun available(): Int {
        return stream.available()
    }
}
