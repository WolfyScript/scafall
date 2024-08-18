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
package com.wolfyscript.scaffolding.spigot.platform.particles.shapes

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.google.common.base.Preconditions
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeResolver
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed
import org.bukkit.util.Vector
import java.util.function.Consumer

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["key"])
abstract class Shape protected constructor(key: Key) : Keyed {
    private val key: Key

    init {
        Preconditions.checkArgument(
            !key.value.isEmpty() && !key.namespace.isEmpty(),
            "Invalid NamespacedKey! Namespaced cannot be null or empty!"
        )
        this.key = key
    }

    /**
     * Applies the [<] for all vertices of the shape.<br></br>
     * Resource intensive tasks should be done beforehand, as this method might be called each tick.<br></br>
     * The consumer might be nested like in [ShapeComplexRotation] to rotate all vertices.<br></br>
     * Because of that the vertices should be copied, so changes won't get reflected to this shape vertices (If they were cached)!
     *
     * @param time The current time value from the timer.
     * @param drawVector The consumer that calculates the vector and spawns the particles.
     */
    abstract fun drawVectors(time: Double, drawVector: Consumer<Vector>)

    @JsonIgnore
    override fun key(): Key {
        return key
    }

    enum class Direction {
        X_AXIS, Y_AXIS, Z_AXIS
    }
}
