/*
 * Copyright (c) 2013, Daniel Murphy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jbox2d.collision.broadphase

import org.jbox2d.callbacks.TreeCallback
import org.jbox2d.callbacks.TreeRaycastCallback
import org.jbox2d.collision.RaycastInput
import org.tobi29.math.AABB2
import org.tobi29.math.vector.Vector2d

interface BroadPhaseStrategy {
    /**
     * Compute the height of the binary tree in O(N) time. Should not be called often.
     */
    val height: Int

    /**
     * Get the maximum balance of an node in the tree. The balance is the difference in height of the
     * two children of a node.
     */
    val maxBalance: Int

    /**
     * Get the ratio of the sum of the node areas to the root area.
     */
    val areaRatio: Double

    /**
     * Create a proxy. Provide a tight fitting AABB and a userData pointer.
     * @param aabb
     * @param userData
     * @return
     */
    fun createProxy(aabb: AABB2, userData: Any?): Int

    /**
     * Destroy a proxy
     * @param proxyId
     */
    fun destroyProxy(proxyId: Int)

    /**
     * Move a proxy with a swepted AABB. If the proxy has moved outside of its fattened AABB, then the
     * proxy is removed from the tree and re-inserted. Otherwise the function returns immediately.
     *
     * @return true if the proxy was re-inserted.
     */
    fun moveProxy(proxyId: Int, aabb: AABB2, displacement: Vector2d): Boolean

    fun getUserData(proxyId: Int): Any?

    fun getFatAABB(proxyId: Int): AABB2

    /**
     * Query an AABB for overlapping proxies. The callback class is called for each proxy that
     * overlaps the supplied AABB.
     * @param callback
     * @param aabb
     */
    fun query(callback: TreeCallback, aabb: AABB2)

    /**
     * Ray-cast against the proxies in the tree. This relies on the callback to perform a exact
     * ray-cast in the case were the proxy contains a shape. The callback also performs the any
     * collision filtering. This has performance roughly equal to k * log(n), where k is the number of
     * collisions and n is the number of proxies in the tree.
     * @param input the ray-cast input data. The ray extends from p1 to p1 + maxFraction * (p2 - p1).
     * @param callback a callback class that is called for each proxy that is hit by the ray.
     */
    fun raycast(callback: TreeRaycastCallback, input: RaycastInput)

    /**
     * Compute the height of the tree.
     */
    fun computeHeight(): Int
}
