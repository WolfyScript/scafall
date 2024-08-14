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
package com.wolfyscript.scaffolding.data

import com.wolfyscript.scaffolding.function.ReceiverBiFunction
import com.wolfyscript.scaffolding.function.ReceiverFunction
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed
import kotlin.reflect.KClass

class DataKey<T : Any, V : DataHolder<V>>(
    val type: KClass<T>,
    private val key: Key,
    private val fetcher: ReceiverFunction<V, T?> = ReceiverFunction { null },
    private val applier: ReceiverBiFunction<V, T, V> = ReceiverBiFunction { this }
) : Keyed {

    fun readFrom(source: V): T? {
        return with(fetcher) { source.apply() }
    }

    fun writeTo(value: T, target: V) {
        with(applier) {
            target.apply(value)
        }
    }

    override fun key(): Key = key

}
