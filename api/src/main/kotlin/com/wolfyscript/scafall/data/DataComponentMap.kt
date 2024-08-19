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

/**
 * A DataComponentMap contains the data applied to a [DataHolder].
 * Each data component is associated with a unique key, and can be fetched using Keys (e.g. [ItemStack Data Keys][ItemStackDataKeys]).
 *
 * By default, this will try to convert the calls to the platform specific ItemStack APIs.
 * In 1.20.5+ this makes use of the data components as much as the platform allows (e.g. ItemMeta on Spigot).
 */
interface DataComponentMap<H : DataHolder<H>> {

    /**
     * Gets the data from the map associated with the specified key.
     *
     * @return The data associated with the key; null otherwise.
     */
    fun <T : Any> get(key: DataKey<T,H>): T?

    fun <T : Any> getOrDefault(key: DataKey<T,H>, def: T): T {
        val value: T? = this.get(key)
        return value ?: def
    }

    fun <T : Any> set(key: DataKey<T, H>, data: T)

    fun remove(key: DataKey<*, H>) : Boolean

    fun has(key: DataKey<*, H>): Boolean

    fun keySet(): Set<DataKey<*, H>>

    fun size(): Int
}
