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
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.eval.operator.Operator
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.nbt.NBTTagConfig
import com.wolfyscript.scafall.wrappers.world.items.data.ItemDataKeyRegistry
import java.lang.IllegalStateException

/**
 * Includes all the Registries inside WolfyUtilities.<br></br>
 * <br></br>
 * To use the registries you need to get an instance of this class.<br></br>
 * You should always try to not use the static method, as it can make the code less maintainable.<br></br>
 * If it is possible to access the instance of your WU API, then that should be used instead!<br></br>
 * <br></br>
 *
 * **Get an instance:**
 *
 *  * (**Recommended**) via your API instance [WolfyUtils.getRegistries]
 *  * via the core [WolfyCore.getRegistries]
 *
 */
abstract class Registries(val core: Scafall) {

    private val REGISTRIES_BY_TYPE: MutableMap<Class<out Keyed>, Registry<*>> = HashMap()
    private val REGISTRIES_BY_KEY: MutableMap<Key, Registry<*>> = HashMap()

    val valueProviders: TypeRegistry<ValueProvider<*>> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "value_providers"), this)
    val operators: TypeRegistry<Operator> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "operators"), this)
    val nbtTagConfigs: TypeRegistry<NBTTagConfig> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "nbt_configs"), this)
    val itemDataKeyRegistry: ItemDataKeyRegistry = ItemDataKeyRegistry(Key.key(Key.SCAFFOLDING_NAMESPACE, "data_components/item"), this)

    fun indexTypedRegistry(registry: Registry<*>) {
        Preconditions.checkArgument(!REGISTRIES_BY_KEY.containsKey(registry.key), "A registry with the key \"" + registry.key + "\" already exists!")
        REGISTRIES_BY_KEY[registry.key] = registry

        //Index them by type if available
        if (registry is RegistrySimple<*> && registry.type != null) {
            Preconditions.checkArgument(!REGISTRIES_BY_TYPE.containsKey(registry.type), "A registry with that type already exists!")
            REGISTRIES_BY_TYPE[registry.type] = registry
        }
    }

    /**
     * Gets a Registry by the type it contains.
     * The Registry has to be created with the class of the type (See: [RegistrySimple.RegistrySimple]).
     *
     * @param type The class of the type the registry contains.
     * @param <V> The type the registry contains.
     * @return The registry of the specific type; or null if not available.
    </V> */
    fun <V : Keyed> getByType(type: Class<V>): Registry<V>? {
        return REGISTRIES_BY_TYPE[type] as Registry<V>?
    }

    fun getByKey(key: Key): Registry<*>? {
        return REGISTRIES_BY_KEY[key]
    }

    fun <V : Registry<*>> getByKeyOfType(key: Key, registryType: Class<V>): V {
        val registry = getByKey(key)
        return registryType.cast(registry) ?: throw IllegalStateException("Requested registry $key or type $registryType could not be found!")
    }

}
