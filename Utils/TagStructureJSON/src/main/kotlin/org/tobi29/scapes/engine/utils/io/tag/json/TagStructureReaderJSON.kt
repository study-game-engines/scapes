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

package org.tobi29.scapes.engine.utils.io.tag.json

import org.tobi29.scapes.engine.utils.io.ByteStreamInputStream
import org.tobi29.scapes.engine.utils.io.ReadableByteStream
import org.tobi29.scapes.engine.utils.io.tag.TagStructure
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.util.*
import javax.json.Json
import javax.json.stream.JsonParser

class TagStructureReaderJSON(streamIn: InputStream) : TagStructureJSON(), AutoCloseable {
    private val reader: JsonParser

    constructor(stream: ReadableByteStream) : this(
            ByteStreamInputStream(stream)) {
    }

    init {
        reader = Json.createParser(streamIn)
        val event = reader.next()
        if (event != JsonParser.Event.START_OBJECT) {
            throw IOException("No root object start")
        }
    }

    override fun close() {
        reader.close()
    }

    fun readStructure(tagStructure: TagStructure) {
        while (true) {
            val keyEvent = reader.next()
            when (keyEvent) {
                JsonParser.Event.END_OBJECT -> {
                    return
                }
                JsonParser.Event.KEY_NAME -> {
                    // Continue
                }
                else -> throw IOException("Expected key, but found: $keyEvent")
            }
            val key = reader.string
            val event = reader.next()
            when (event) {
                JsonParser.Event.START_OBJECT -> {
                    val childStructure = TagStructure()
                    readStructure(childStructure)
                    tagStructure.setStructure(key, childStructure)
                }
                JsonParser.Event.START_ARRAY -> {
                    readList(key, tagStructure)
                }
                JsonParser.Event.VALUE_NUMBER -> {
                    if (reader.isIntegralNumber) {
                        tagStructure.setNumber(key, reader.long)
                    } else {
                        tagStructure.setNumber(key, unarmor(reader.bigDecimal))
                    }
                }
                JsonParser.Event.VALUE_STRING -> {
                    tagStructure.setString(key, reader.string)
                }
                JsonParser.Event.VALUE_FALSE -> {
                    tagStructure.setBoolean(key, false)
                }
                JsonParser.Event.VALUE_TRUE -> {
                    tagStructure.setBoolean(key, true)
                }
                else -> throw IOException("Unexpected event: $event")
            }
        }
    }

    fun readList(key: String,
                 parentStructure: TagStructure) {
        val next = reader.next()
        when (next) {
            JsonParser.Event.END_ARRAY -> {
                parentStructure.setList(key, emptyList())
            }
            JsonParser.Event.START_OBJECT -> {
                val list = ArrayList<TagStructure>()
                readStructureList(list)
                parentStructure.setList(key, list)
            }
            JsonParser.Event.VALUE_NUMBER -> {
                val bits = reader.int
                when (bits) {
                    8 -> {
                        parentStructure.setByteArray(key, *readByteArray())
                    }
                    else -> throw IOException(
                            "Unknown array identifier: " + bits)
                }
            }
            else -> throw IOException("Illegal contents of array: " + next)
        }
    }

    fun readStructureList(list: MutableList<TagStructure>) {
        while (true) {
            val childStructure = TagStructure()
            readStructure(childStructure)
            list.add(childStructure)
            val event = reader.next()
            if (event == JsonParser.Event.END_ARRAY) {
                return
            } else if (event != JsonParser.Event.START_OBJECT) {
                throw IOException("Invalid event in list: $event")
            }
        }
    }

    fun readByteArray(): ByteArray {
        var event = reader.next()
        var array = ByteArray(1024)
        var i = 0
        while (event != JsonParser.Event.END_ARRAY) {
            if (event == JsonParser.Event.VALUE_NUMBER) {
                if (array.size == i) {
                    val newArray = ByteArray(array.size shl 2)
                    System.arraycopy(array, 0, newArray, 0, array.size)
                    array = newArray
                }
                array[i++] = reader.int.toByte()
            } else {
                throw IOException("Illegal contents of byte array")
            }
            event = reader.next()
        }
        val outArray = ByteArray(i)
        System.arraycopy(array, 0, outArray, 0, outArray.size)
        return outArray
    }

    private fun unarmor(value: BigDecimal): Double {
        if (value.compareTo(TagStructureJSON.POSITIVE_INFINITY) == 0) {
            return Double.POSITIVE_INFINITY
        } else if (value.compareTo(TagStructureJSON.NEGATIVE_INFINITY) == 0) {
            return Double.NEGATIVE_INFINITY
        } else if (value.compareTo(TagStructureJSON.NAN) == 0) {
            return Double.NaN
        }
        return value.toDouble()
    }
}
