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
package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed

/**
 * This registry allows you to register classes under NamespacedKeys. <br></br>
 * It is similar to the [Registry], with the difference that it stores the classes of the type. <br></br>
 * To get a new instance of an entry you must use [.create] or create it manually. <br></br>
 *
 *
 * Main use case of this registry would be to prevent using the [Registry] with default objects <br></br>
 * and prevents unwanted usage of those values, as this registry enforces to create new instances.
 *
 * @param <V> The type of the values.
</V> */
interface TypeRegistry<V : Keyed> : Registry<Class<out V>> {

    /**
     * This method creates a new instance of the specific class, if it is available. <br></br>
     * If class for the key is not found it will return null. <br></br>
     * Default implementation looks for the default constructor.
     *
     * @param key The [NamespacedKey] of the value.
     * @return A new instance of the class.
     */
    fun create(key: Key): V?

}
