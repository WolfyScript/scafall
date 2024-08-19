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
package com.wolfyscript.scafall.spigot.platform.world.items.actions

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scafall.spigot.platform.particles.ParticleAnimation

class ActionParticleAnimation protected constructor() :
    Action<DataLocation>(
        KEY,
        DataLocation::class.java
    ) {
    private var animation: ParticleAnimation? = null

    override fun execute(core: Scafall, data: DataLocation) {
        if (data is DataPlayer) {
            animation?.spawn(data.player)
            return
        }
        animation?.spawn(data.location)
    }

    fun setAnimation(animation: ParticleAnimation) {
        this.animation = animation
    }

    companion object {
        val KEY: Key = scaffoldingKey("location/particle_animation")
    }
}
