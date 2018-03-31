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
/**
 * Created at 5:20:39 AM Jan 22, 2011
 */
package org.jbox2d.dynamics.joints

import org.jbox2d.dynamics.World

/**
 * Gear joint definition. This definition requires two existing revolute or prismatic joints (any
 * combination will work). The provided joints must attach a dynamic body to a static body.
 *
 * @author Daniel Murphy
 */
class GearJointDef : JointDef<GearJoint>() {
    /**
     * The first revolute/prismatic joint attached to the gear joint.
     */
    var joint1: Joint? = null

    /**
     * The second revolute/prismatic joint attached to the gear joint.
     */
    var joint2: Joint? = null

    /**
     * Gear ratio.
     *
     * @see GearJoint
     */
    var ratio: Double = 0.0

    init {
        joint1 = null
        joint2 = null
    }

    override fun create(world: World): GearJoint =
        GearJoint(world.pool, this)
}
