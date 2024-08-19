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
package com.wolfyscript.scafall.spigot.platform.world.items.meta

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scafall.config.jackson.JacksonUtil.objectMapper
import com.wolfyscript.scafall.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeResolver
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import com.wolfyscript.scafall.spigot.platform.world.items.ItemBuilder
import java.lang.reflect.InvocationTargetException
import java.util.*

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder("key")
abstract class Meta protected constructor(private val key: Key) : Keyed {
    open var option: MetaSettings.Option = MetaSettings.Option.EXACT

    @JsonIgnore
    var availableOptions: List<MetaSettings.Option> = java.util.List.of(MetaSettings.Option.EXACT)
        private set

    @get:JsonIgnore
    val isExact: Boolean
        get() = option == MetaSettings.Option.EXACT

    protected fun setAvailableOptions(vararg options: MetaSettings.Option) {
        availableOptions = listOf(*options)
    }

    abstract fun check(item: CustomItem, itemOther: ItemBuilder): Boolean

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val meta = o as Meta
        return key == meta.key && option == meta.option && availableOptions == meta.availableOptions
    }

    override fun hashCode(): Int {
        return Objects.hash(key, option, availableOptions)
    }

    @JsonIgnore
    override fun key(): Key {
        return key
    }

    @Deprecated("")
    class Provider<M : Meta?>(namespacedKey: Key, type: Class<M>) :
        Keyed {
        private val namespacedKey: Key
        private val type: Class<M>

        init {
            Objects.requireNonNull(
                type,
                "Cannot initiate Meta \"$namespacedKey\" with a null type!"
            )
            this.namespacedKey = namespacedKey
            this.type = type
        }

        override fun key(): Key {
            return namespacedKey
        }

        fun provide(): M? {
            try {
                return type.getDeclaredConstructor().newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
            return null
        }

        @Deprecated("")
        fun parse(node: JsonNode?): M {
            return objectMapper.convertValue(node, type)
        }
    }
}
