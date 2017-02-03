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

package org.tobi29.scapes.engine.server

import kotlinx.coroutines.experimental.*
import mu.KLogging
import org.tobi29.scapes.engine.utils.task.Joiner
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Class for processing non-blocking connections
 * @param connection The [ConnectionManager] that holds this worker
 * @param joiner The [Joiner.SelectorJoinable] used for idling
 * @param maxWorkerSleep Maximum sleep time in milliseconds
 */
class ConnectionWorker(
        /**
         * The [ConnectionManager] that holds this worker
         */
        val connection: ConnectionManager,
        /**
         * The [Joiner.SelectorJoinable] used for idling
         */
        val joiner: Joiner.SelectorJoinable,
        private val maxWorkerSleep: Long,
        private val thread: Thread = Thread.currentThread()) : CoroutineDispatcher() {
    private val connectionQueue = ConcurrentLinkedQueue<suspend CoroutineScope.(Connection) -> Unit>()
    private val connections = ArrayList<ConnectionHandle>()

    private val queue = ConcurrentLinkedQueue<() -> Boolean>()

    /**
     * Returns an estimate for how many connections this worker is processing
     *
     * Note: This technically violates thread-safety, hence the value should be
     * taken with a grain of salt
     * @return The amount of connections on this worker
     */
    val connectionCount: Int
        get() = connections.size

    /**
     * Adds a new connection to this worker
     * @param block Code that will be executed on this worker's thread
     */
    fun addConnection(block: suspend CoroutineScope.(Connection) -> Unit) {
        connectionQueue.add(block)
        joiner.wake()
    }

    /**
     * Executes this worker on the current thread
     *
     * **Note:** Should never be called twice or concurrently
     */
    fun run() {
        while (!joiner.marked) {
            process()
            if (connectionQueue.isNotEmpty()) {
                while (connectionQueue.isNotEmpty()) {
                    val coroutine = connectionQueue.poll()
                    val requestClose = AtomicBoolean()
                    val timeout = AtomicLong(System.currentTimeMillis() + 20000)
                    val connection = Connection(requestClose, timeout)
                    val job = launch(this) { coroutine(connection) }
                    val close = ConnectionHandle(job, requestClose)
                    connections.add(close)
                    launch(this) {
                        while (job.isActive) {
                            if (System.currentTimeMillis() > timeout.get()) {
                                job.cancel(IOException("Timeout"))
                            }
                            yield()
                        }
                    }
                    job.onCompletion {
                        connections.remove(close)
                    }
                }
            } else if (!joiner.marked) {
                val sleep = if (connections.isEmpty()) 0 else maxWorkerSleep
                joiner.sleep(sleep)
            }
        }
        connections.forEach { it.requestClose.set(true) }
        val stopTimeout = System.nanoTime()
        while (connections.isNotEmpty() && System.nanoTime() - stopTimeout < 10000000000L) {
            process()
            if (!joiner.marked) {
                joiner.sleep(10)
            }
        }
        connections.forEach { it.job.cancel(IOException("Killing worker")) }
        while (connections.isNotEmpty()) {
            process()
        }
    }

    private fun process() {
        if (queue.isNotEmpty()) {
            queue.add { true }
            while (queue.isNotEmpty()) {
                if (queue.poll()()) {
                    break
                }
            }
        }
    }

    override fun dispatch(context: CoroutineContext,
                          block: Runnable) {
        queue.add {
            try {
                block.run()
            } catch (e: CancellationException) {
                logger.warn { "Job cancelled: ${e.message}" }
            }
            false
        }
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return thread == Thread.currentThread()
    }

    companion object : KLogging()

    private class ConnectionHandle(val job: Job,
                                   val requestClose: AtomicBoolean)
}
