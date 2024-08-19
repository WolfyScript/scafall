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
package com.wolfyscript.scafall.spigot.platform.particles.pos

import org.bukkit.Location
import org.bukkit.entity.Entity

class ParticlePosEntity : ParticlePos {
    private val entity: Entity

    constructor(entity: Entity) : super() {
        this.entity = entity
    }

    constructor(pos: ParticlePosEntity) : super(pos) {
        this.entity = pos.entity
    }

    override val location: Location
        get() = entity.location.add(offset)

    override fun shallowCopy(): ParticlePosEntity? {
        return ParticlePosEntity(this)
    }
}
