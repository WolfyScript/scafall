/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scafall.spigot.platform.particles.animators

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import com.wolfyscript.scafall.spigot.platform.particles.ParticleEffect
import com.wolfyscript.scafall.spigot.platform.particles.timer.Timer
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * This basic animator doesn't actually animate anything. It spawns the effect at the specified location (origin).
 */
class AnimatorBasic : Animator(KEY) {
    override fun draw(timer: Timer.Runner, effect: ParticleEffect?, origin: Location, player: Player?) {
        timer.increase()
        spawnParticle(effect!!, origin, player)
    }


    companion object {
        val KEY: Key = defaultKey("basic")
    }
}
