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
package com.wolfyscript.scaffolding.registry

import com.google.common.base.Preconditions
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.function.Supplier

abstract class AbstractTypeRegistry<M : MutableMap<Key, Class<out V>>, V : Keyed>(
    override val key: Key,
    protected val map: M,
    protected val registries: Registries
) : TypeRegistry<V> {

    init {
        registries.indexTypedRegistry(this)
    }

    constructor(key: Key, mapSupplier: Supplier<M>, registries: Registries) : this(
        key,
        mapSupplier.get(),
        registries
    )

    override fun get(key: Key): Class<out V>? {
        return map[key]
    }

    override fun create(key: Key): V? {
        val clazz = get(key)
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun register(key: Key, value: Class<out V>) {
        Preconditions.checkState(!map.containsKey(key), "namespaced key '%s' already has an associated value!", key)
        map.put(key, value)
    }

    override fun register(value: Class<out V>) {
        val id: Key = Key.parse(StaticNamespacedKey.KeyBuilder.createKeyString(value))
        register(id, value)
    }

    override fun iterator(): Iterator<Class<out V>> {
        return map.values.iterator()
    }

    override fun keySet(): Set<Key> {
        return Collections.unmodifiableSet(map.keys)
    }

    override fun values(): Collection<Class<out V>> {
        return Collections.unmodifiableCollection(map.values)
    }

    override fun entrySet(): Set<Map.Entry<Key, Class<out V>>> {
        return Collections.unmodifiableSet<Map.Entry<Key, Class<out V>>>(map.entries)
    }

}
