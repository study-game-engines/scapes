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

package org.tobi29.scapes.engine.backends.openal.openal.internal

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.SendChannel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.yield
import org.tobi29.scapes.engine.ScapesEngine
import org.tobi29.scapes.engine.backends.openal.openal.OpenAL
import org.tobi29.scapes.engine.backends.openal.openal.OpenALSoundSystem
import org.tobi29.codec.AudioBuffer
import org.tobi29.codec.AudioStream
import org.tobi29.codec.ReadableAudioStream
import org.tobi29.codec.toPCM16
import org.tobi29.math.vector.Vector3d
import org.tobi29.scapes.engine.sound.AudioController
import org.tobi29.scapes.engine.sound.AudioFormat
import org.tobi29.scapes.engine.sound.VolumeChannel
import org.tobi29.scapes.engine.sound.VolumeChannelEnvironment
import org.tobi29.stdex.assert
import org.tobi29.io.*
import org.tobi29.logging.KLogging
import kotlin.coroutines.experimental.CoroutineContext

internal class OpenALStreamAudio(
        private val engine: ScapesEngine,
        private val asset: ReadSource,
        private val channel: String,
        private val pos: Vector3d,
        private val velocity: Vector3d,
        private val state: Boolean,
        private val hasPosition: Boolean,
        private val controller: OpenALAudioController
) : OpenALAudio,
        AudioController by controller {
    private val streamBuffer = MemoryViewStream<ByteViewE>(
            { engine.container.allocateNative(it) })
    private var source = -1
    private var queued = 0
    private var decodeActor: Pair<SendChannel<AudioBuffer>, Channel<AudioBuffer>>? = null

    constructor(engine: ScapesEngine,
                asset: ReadSource,
                channel: String,
                pos: Vector3d,
                velocity: Vector3d,
                state: Boolean,
                hasPosition: Boolean,
                pitch: Double,
                gain: Double,
                referenceDistance: Double,
                rolloffFactor: Double
    ) : this(engine, asset, channel, pos, velocity, state, hasPosition,
            OpenALAudioController(pitch, gain, referenceDistance,
                    rolloffFactor))

    override fun poll(sounds: OpenALSoundSystem,
                      openAL: OpenAL,
                      listenerPosition: Vector3d,
                      delta: Double): Boolean {
        if (source == -1) {
            source = openAL.createSource()
            if (source == -1) {
                return true
            }
            controller.configure(openAL, source, sounds.volume(channel), true)
            openAL.setLooping(source, false)
            sounds.position(openAL, source, pos, hasPosition)
            openAL.setVelocity(source, velocity)
            openAL.setReferenceDistance(source, 1.0)
            openAL.setMaxDistance(source, Double.POSITIVE_INFINITY)
            decodeActor = decodeActor(sounds, asset, state)
        }
        val (decode, buffers) = decodeActor ?: return true
        if (buffers.isClosedForReceive) return true
        try {
            controller.configure(openAL, source, sounds.volume(channel))
            while (queued < 3) {
                val buffer = buffers.poll() ?: break
                val audioBuffer = openAL.createBuffer()
                store(openAL, buffer, audioBuffer)
                buffer.clear()
                if (!decode.offer(buffer))
                    throw IllegalStateException("Buffer lost")
                openAL.queue(source, audioBuffer)
                queued++
            }
            if (queued > 0 && !openAL.isPlaying(source)) {
                openAL.play(source)
            }
            var finished = openAL.getBuffersProcessed(source)
            while (finished > 0) {
                val unqueued = openAL.unqueue(source)
                val buffer = buffers.poll()
                if (buffer == null) {
                    openAL.deleteBuffer(unqueued)
                    queued--
                    break
                }
                store(openAL, buffer, unqueued)
                buffer.clear()
                if (!decode.offer(buffer))
                    throw IllegalStateException("Buffer lost")
                openAL.queue(source, unqueued)
                finished--
            }
        } catch (e: IOException) {
            logger.warn { "Failed to stream music: $e" }
            stop(sounds, openAL)
            return true
        }
        return false
    }

    override fun isPlaying(channel: VolumeChannel) =
            VolumeChannelEnvironment.run {
                this@OpenALStreamAudio.channel in channel
            }

    override fun stop(sounds: OpenALSoundSystem,
                      openAL: OpenAL) {
        if (source != -1) {
            openAL.stop(source)
            var queued = openAL.getBuffersQueued(source)
            while (queued-- > 0) {
                openAL.deleteBuffer(openAL.unqueue(source))
                this.queued--
            }
            openAL.deleteSource(source)
            source = -1
            assert { this.queued == 0 }
        }
        try {
            decodeActor?.first?.close()
        } catch (e: Exception) {
            logger.warn { "Failed to close stream: $e" }
        }
        decodeActor = null
    }

    private fun store(openAL: OpenAL,
                      buffer: AudioBuffer,
                      audioBuffer: Int) {
        buffer.toPCM16 { streamBuffer.putShort(it) }
        streamBuffer.flip()
        openAL.storeBuffer(audioBuffer,
                if (buffer.channels() > 1) AudioFormat.STEREO
                else AudioFormat.MONO, streamBuffer.bufferSlice(),
                buffer.rate())
        streamBuffer.reset()
    }

    companion object : KLogging()
}

fun decodeActor(context: CoroutineContext,
                asset: ReadSource,
                state: Boolean): Pair<SendChannel<AudioBuffer>, Channel<AudioBuffer>> {
    val output = Channel<AudioBuffer>()
    val actor = actor<AudioBuffer>(context, 1) {
        try {
            do {
                asset.channel().use { dataChannel ->
                    AudioStream.create(dataChannel,
                            asset.mimeType()).use { stream ->
                        loop@ while (true) {
                            val buffer = channel.receive()
                            while (true) {
                                when (stream.get(buffer)) {
                                    ReadableAudioStream.Result.YIELD -> yield()
                                    ReadableAudioStream.Result.BUFFER -> {
                                        output.send(buffer)
                                        continue@loop
                                    }
                                    ReadableAudioStream.Result.EOS -> {
                                        output.send(buffer)
                                        break@loop
                                    }
                                }
                            }
                        }
                    }
                }
            } while (state)
        } finally {
            output.close()
        }
    }
    repeat(1) { actor.offer(AudioBuffer(4096)) }
    return actor to output
}
