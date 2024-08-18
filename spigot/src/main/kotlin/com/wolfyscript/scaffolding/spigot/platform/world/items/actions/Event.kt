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
package com.wolfyscript.scaffolding.spigot.platform.world.items.actions

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeResolver
import com.wolfyscript.scaffolding.config.jackson.OptionalKeyReference
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed
import java.util.*

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["key"])
@OptionalKeyReference(serializeAsKey = false, registryKey = "wolfyutilities:custom_item/events/values")
abstract class Event<T : Data> protected constructor(
    private val key: Key, @field:JsonIgnore @get:JsonIgnore val dataType: Class<T>
) :
    Keyed {
    private var actions: List<Action<in T>> = listOf()

    open fun call(core: Scaffolding, data: T) {
        for (action in actions!!) {
            action.execute(core, data)
        }
    }

    @JsonIgnore
    fun isApplicable(action: Action<*>): Boolean {
        return action.dataType.isAssignableFrom(dataType)
    }

    fun setActions(actions: List<Action<in T>>) {
        this.actions = actions.stream().filter { action: Action<in T> -> this.isApplicable(action) }.toList()
    }

    fun getActions(): List<Action<in T>> {
        return actions
    }

    @JsonIgnore
    override fun key(): Key {
        return key
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Event<*>
        return key == that.key
    }

    override fun hashCode(): Int {
        return Objects.hash(key)
    }
}
