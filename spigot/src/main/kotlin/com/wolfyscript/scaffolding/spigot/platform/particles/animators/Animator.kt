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
package com.wolfyscript.scaffolding.spigot.platform.particles.animators

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeResolver
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed
import com.wolfyscript.scaffolding.spigot.platform.particles.ParticleEffect
import com.wolfyscript.scaffolding.spigot.platform.particles.timer.Timer
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * The Animator is used to draw/spawn the [ParticleEffect]s.<br></br>
 * It uses the [Timer.Runner] to calculate positions of particles dependent on the time passed, and possible other data.<br></br>
 *
 *
 */
@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["key"])
abstract class Animator protected constructor(private val key: Key) : Keyed {

    /**
     * Spawns the [ParticleEffect] at the specified location in the world.
     * This will send the particles to all players in range.
     *
     * @param effect The effect to spawn.
     * @param location The location to spawn it on.
     */
    protected fun spawnParticle(effect: ParticleEffect, location: Location) {
        if (location.world != null) {
            location.world.spawnParticle(
                effect.particle,
                location,
                effect.count,
                effect.offset.x,
                effect.offset.y,
                effect.offset.z,
                effect.extra,
                effect.data
            )
        }
    }

    /**
     * Spawns the [ParticleEffect] at the specified location in the world.
     * If a player is specified only that player will receive the particles, else [.spawnParticle]
     *
     * @param effect The effect to spawn.
     * @param location The location to spawn it on.
     * @param player The player to send the particles to. If null, sends it to all players in range (If the value is always null use [.spawnParticle]!).
     */
    protected fun spawnParticle(effect: ParticleEffect, location: Location, player: Player?) {
        if (player != null) {
            player.spawnParticle(
                effect.particle,
                location,
                effect.count,
                effect.offset.x,
                effect.offset.y,
                effect.offset.z,
                effect.extra,
                effect.data
            )
        } else {
            spawnParticle(effect, location)
        }
    }

    /**
     * Called each time a [ParticleEffect] is spawned.<br></br>
     * The [Timer.Runner] contains the current state of the effect, like tick, or other type specific data.
     * This method must call the [Timer.Runner.increase] to increase the timer and get the passed time and continue.
     *
     * @param timer The timer that contains the state of the effect.
     * @param effect The effect that is being spawned.
     * @param origin The origin location of the effect. If the effect is part of an animation this includes the offset.
     * @param player The player to spawn the effect for. Null if no player is specified, which means the effect is spawned for all the players in range.
     */
    abstract fun draw(timer: Timer.Runner, effect: ParticleEffect?, origin: Location, player: Player?)

    @JsonIgnore
    override fun key(): Key {
        return key
    }
}
