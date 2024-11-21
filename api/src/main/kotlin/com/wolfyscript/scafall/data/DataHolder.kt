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

interface DataHolder<H : DataHolder<H, M>, M: DataComponentMap<H>> {

    val data: M

    fun <T : Any> get(key: DataKey<T, in H>): T? = data.get(key)

    interface Mutable<H : Mutable<H>> : DataHolder<H, DataComponentMap.Mutable<H>> {

        /**
         * Sets the value of the specified Key
         */
        fun <T : Any> set(key: DataKey<T, in H>, data: T) = this.data.set(key, data)

    }

    interface Immutable<H : Immutable<H>> : DataHolder<H, DataComponentMap.Immutable<H>> {

        /**
         * Sets the value of the specified Key
         */
        fun <T : Any> set(key: DataKey<T, in H>, data: T) : H = this.data.set(key, data)

    }

}
