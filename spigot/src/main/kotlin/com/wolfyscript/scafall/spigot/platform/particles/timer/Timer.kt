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
package com.wolfyscript.scafall.spigot.platform.particles.timer

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeResolver
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["key", "startValue", "stopValue"])
abstract class Timer protected constructor(private val key: Key) : Keyed {
    /**
     * The start value is the initial time value of the supplier.
     * Default value is 0.0.
     *
     * @return The initial time.
     */
    var startValue: Double = 0.0

    /**
     * The value at which point the supplier will stop increasing.
     *
     * @return The value at which the supplier stops.
     */
    open var stopValue: Double = 0.0

    abstract fun createRunner(): Runner

    @JsonIgnore
    override fun key(): Key {
        return key
    }

    /**
     * This object contains the actual state of the particle effect.
     * Each time an effect is spawned a new Runner is created with the specified start time.
     *
     */
    abstract inner class Runner protected constructor() {
        var time: Double
            protected set

        init {
            this.time = startValue
        }

        /**
         * Increases the time of the runner by the specified increment.
         *
         * @return The increased time of the runner.
         */
        abstract fun increase(): Double

        open fun shouldStop(): Boolean {
            return time > stopValue
        }
    }
}
