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
 * By default, this will try to convert the calls to the platform specific APIs as best as it can.
 * e.g. for ItemStacks: on Spigot ItemMeta, on Sponge the Key based system
 */
interface DataComponentMap<H : DataHolder<*, *>> {

    /**
     * Gets the data from the map associated with the specified key.
     *
     * @return The data associated with the key; null otherwise.
     */
    fun <T : Any> get(key: DataKey<T, in H>): T?

    fun <T : Any> getOrDefault(key: DataKey<T, in H>, def: T): T {
        val value: T? = this.get(key)
        return value ?: def
    }

    /**
     * Checks if the DataKey has an associated value
     */
    fun has(key: DataKey<*, in H>): Boolean

    fun keys(): Set<DataKey<*, in H>>

    interface Mutable<H: DataHolder.Mutable<*>> : DataComponentMap<H> {

        /**
         * Sets the value of the specified Key
         */
        fun <T : Any> set(key: DataKey<T, in H>, data: T)

        /**
         * Removes the value association of the specified Key
         */
        fun remove(key: DataKey<*, in H>) : Boolean

    }

    interface Immutable<H: DataHolder.Immutable<*>> : DataComponentMap<H> {

        /**
         * Sets the value of the specified Key
         */
        fun <T : Any> set(key: DataKey<T, in H>, data: T) : H

    }

}
