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
package com.wolfyscript.scafall.spigot.platform.particles

import com.wolfyscript.scafall.spigot.platform.world.items.ParticleContent.PlayerSettings
import java.util.function.BiConsumer

/**
 * Represents the location at which an Animation/Effect can be spawned at.
 */
enum class ParticleLocation(private val applyOldPlayerAnimation: BiConsumer<PlayerSettings, ParticleAnimation>?) {
    LOCATION(null),
    ENTITY(null),
    BLOCK(null),
    PLAYER(null);

    fun applyOldPlayerAnimation(playerSettings: PlayerSettings, animation: ParticleAnimation) {
        applyOldPlayerAnimation?.accept(playerSettings, animation)
    }

    val isDeprecated: Boolean
        get() = applyOldPlayerAnimation != null
}
