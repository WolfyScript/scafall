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

/**
 * A registry to register objects under specified namespaced keys.
 * This allows for easier management of custom content etc.
 *
 * @param <V>
</V> */
interface Registry<V> : Iterable<V> {
    /**
     * Get the value of the registry by its [NamespacedKey]
     *
     * @param key The [NamespacedKey] of the value.
     * @return The value of the [NamespacedKey].
     */
    operator fun get(key: Key): V?

    /**
     * Receives the key under which the value is registered in this Registry.<br></br>
     * The default implementation is quite inefficient as it uses the stream filter method
     * to find the matching value.<br></br>
     * Additionally, it only returns the key of the first match. Therefor this only works reliably if the values are unique too!<br></br>
     * Some Registry implementations or extensions might have exactly that behaviour.<br></br>
     * <br></br>
     * Some implementations are:<br></br>
     * [UniqueTypeRegistrySimple]
     *
     * @param value The value to get the key for.
     * @return The key for the registered value or null if not found.
     */
    fun getKey(value: V): Key? {
        return entrySet().stream().filter { entry: Map.Entry<Key?, V> -> entry.value == value }
            .map<Key?> { it.key }.findFirst().orElse(null)
    }

    /**
     * Register a value with a [NamespacedKey] to this registry.
     * You can't override values that are already registered under the same [NamespacedKey]!
     *
     * @param key   The [NamespacedKey] to register it to.
     * @param value The value to register.
     */
    fun register(key: Key, value: V)

    /**
     * Registers a value with its contained [NamespacedKey] it gets via the [V.getNamespacedKey] method.
     * @param value The value to register.
     */
    fun register(value: V)

    fun keySet(): Set<Key>

    fun values(): Collection<V>

    fun entrySet(): Set<Map.Entry<Key?, V>>

    val key: Key
}
