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
package com.wolfyscript.scafall.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import kotlin.reflect.KClass

class DataKey<T : Any, V : DataHolder<*, *>>(
    val type: KClass<T>,
    private val key: Key,
    private val fetcher: V.() -> T? = { null },
    private val applier: V.(T) -> V = { this }
) : Keyed {

    fun readFrom(source: V): T? {
        return source.fetcher()
    }

    fun writeTo(value: T, target: V) {
        target.applier(value)
    }

    override fun key(): Key = key

}
