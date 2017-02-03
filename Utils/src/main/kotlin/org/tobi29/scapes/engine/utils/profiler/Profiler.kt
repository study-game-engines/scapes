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

package org.tobi29.scapes.engine.utils.profiler

import java8.util.Maps
import org.tobi29.scapes.engine.utils.ThreadLocal
import org.tobi29.scapes.engine.utils.profiler.spi.ProfilerDispatcherProvider
import java.util.*

class Profiler private constructor(thread: Thread) {
    private val rootNode: Node
    private var node: Node

    init {
        rootNode = Node({ thread.name })
        node = rootNode
        PROFILERS.put(
                thread, this)
    }

    fun enterNode(name: String) {
        node = Maps.computeIfAbsent(node.children, name) {
            Node({ it }, node)
        }
        node.lastEnter = System.nanoTime()
        ProfilerDispatch.dispatchers.forEach { it.enterNode(name) }
    }

    fun exitNode(name: String) {
        val parentNode = node.parent ?: throw IllegalStateException(
                "Profiler stack popped on root node")
        assert(name == node.name())
        node.time += System.nanoTime() - node.lastEnter
        ProfilerDispatch.dispatchers.forEach { it.exitNode(name) }
        node = parentNode
    }

    private fun resetNodes() {
        rootNode.children.clear()
    }

    companion object {
        private val PROFILERS = WeakHashMap<Thread, Profiler>()
        private val INSTANCE = ThreadLocal(::Profiler)
        var enabled = false

        fun current(): Profiler {
            return INSTANCE.get()
        }

        fun node(thread: Thread): Node? {
            return PROFILERS[thread]?.rootNode
        }

        fun reset() {
            PROFILERS.values.forEach { it.resetNodes() }
        }
    }
}

class Node(val name: () -> String,
           val parent: Node? = null) {
    val children: MutableMap<String, Node> = HashMap()
    var lastEnter = 0L
    var time = 0L

    fun time(): Double {
        return time / 1000000000.0
    }
}

inline fun <R> profilerSection(name: String,
                               receiver: () -> R): R {
    val instance = if (Profiler.enabled) Profiler.current() else null
    instance?.enterNode(name)
    try {
        return receiver()
    } finally {
        instance?.exitNode(name)
    }
}

private object ProfilerDispatch {
    val dispatchers = loadService()

    private fun loadService(): List<ProfilerDispatcher> {
        val dispatchers = ArrayList<ProfilerDispatcher>()
        for (dispatcher in ServiceLoader.load(
                ProfilerDispatcherProvider::class.java)) {
            try {
                dispatcher.dispatcher()?.let { dispatchers.add(it) }
            } catch (e: ServiceConfigurationError) {
            }
        }
        return dispatchers
    }
}
