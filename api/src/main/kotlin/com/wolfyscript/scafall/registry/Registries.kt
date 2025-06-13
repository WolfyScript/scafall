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

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scafall.data.ItemDataComponentConverter
import com.wolfyscript.scafall.eval.operator.Operator
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.nbt.NBTTagConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.data.ItemDataKeyRegistry

/**
 * Includes all the Registries builtin Registries for scafall.
 */
abstract class Registries(val core: Scafall) {

    /**
     * A meta-registry that indexes all available Registries
     */
    val registryOfRegistries: Registry<Registry<*>> = UniqueRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "registries/all"))
    /**
     * A meta-registry that indexes all the [TypeRegistry]s associated with their contained types.
     */
    val registryOfTypes: Registry<Class<*>> = RegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "registries/types"))

    //
    // Type Registries
    //
    val valueProviders: TypeRegistry<ValueProvider<*>> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "value_providers"))
    val operators: TypeRegistry<Operator> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "operators"))
    val nbtTagConfigs: TypeRegistry<NBTTagConfig> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "nbt_configs"))
    val itemStackConfigOverrides: TypeRegistry<ItemStackConfig.Override> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "items/config/overrides"))
    val itemStackIdentifiers: TypeRegistry<ItemStackIdentifier> = UniqueTypeRegistrySimple(Key.key(Key.SCAFFOLDING_NAMESPACE, "items/identifiers/type"))

    //
    // Value Registries
    //
    val itemDataKeyRegistry: ItemDataKeyRegistry = ItemDataKeyRegistry(Key.key(Key.SCAFFOLDING_NAMESPACE, "items/data_component_keys"))
    val itemDataComponentConverterRegistry: Registry<ItemDataComponentConverter<*>> = RegistrySimple(Key.defaultKey("data_component/item/converter"))
    val itemStackIdentifierParsers: Registry<ItemStackIdentifier.Parser<*>> = RegistrySimple(Key.defaultKey("items/identifiers/parser"))

    init {
        indexRegistry(valueProviders)
        indexRegistry(operators)
        indexRegistry(nbtTagConfigs)
        indexRegistry(itemStackConfigOverrides)

        KeyedTypeIdResolver.registerTypeRegistry(ItemStackIdentifier::class.java, itemStackIdentifiers)
    }

    inline fun <reified T> indexRegistry(registry: Registry<T>) {
        registryOfRegistries.register(registry.key, registry)
        if (registry is TypeRegistry<*>) {
            registryOfTypes.register(registry.key, T::class.java)
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
    fun <V> getByType(type: Class<V>): TypeRegistry<V>? {
        val registryKey = registryOfTypes.getKey(type) ?: return null
        return registryOfRegistries[registryKey]?.let {
            if (it !is TypeRegistry<*>) {
                null
            } else {
                it as TypeRegistry<V>
            }
        }
    }

    fun getByKey(key: Key): Registry<*>? {
        return registryOfRegistries[key]
    }

    fun <V : Registry<*>> getByKeyOfType(key: Key, registryType: Class<V>): V {
        val registry = getByKey(key)
        return registryType.cast(registry) ?: throw IllegalStateException("Requested registry $key or type $registryType could not be found!")
    }

}
