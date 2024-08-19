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

import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import java.util.*
import java.util.function.Supplier

abstract class AbstractRegistry<M : MutableMap<Key, V>, V : Keyed>(
    override val key: Key,
    protected val map: M,
    protected val registries: Registries,
    val type: Class<V>?
) : Registry<V> {

    constructor(namespacedKey: Key, map: M, registries: Registries) : this(
        namespacedKey,
        map,
        registries,
        null
    )

    constructor(namespacedKey: Key, mapSupplier: Supplier<M>, registries: Registries) : this(
        namespacedKey,
        mapSupplier.get(),
        registries,
        null
    )

    constructor(
        namespacedKey: Key,
        mapSupplier: Supplier<M>,
        registries: Registries,
        type: Class<V>
    ) : this(namespacedKey, mapSupplier.get(), registries, type)

    init {
        registries.indexTypedRegistry(this)
    }

    private fun isTypeOf(type: Class<*>): Boolean {
        return this.type != null && this.type == type
    }

    override fun get(key: Key): V? {
        return map[key]
    }

    override fun register(key: Key, value: V) {
        Preconditions.checkState(!map.containsKey(key), "namespaced key '%s' already has an associated value!", key)
        map.put(key, value)
    }

    override fun register(value: V) {
        register(value.key(), value)
    }

    override fun iterator(): Iterator<V> {
        return map.values.iterator()
    }

    override fun keySet(): Set<Key> {
        return Collections.unmodifiableSet(map.keys)
    }

    override fun values(): Collection<V> {
        return Collections.unmodifiableCollection(map.values)
    }

    override fun entrySet(): Set<Map.Entry<Key, V>> {
        return Collections.unmodifiableSet(map.entries)
    }
}
