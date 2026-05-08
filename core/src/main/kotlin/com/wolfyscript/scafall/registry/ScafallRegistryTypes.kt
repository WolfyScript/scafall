package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.eval.operator.Operator
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.nbt.NBTTagConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig

/**
 * A central registry for defining and accessing various registry types used within the Scafall framework.
 */
object ScafallRegistryTypes {

    val root = Key.scafall("root")

    val valueProviders = typeRegistry<ValueProvider<*>>("value_providers")

    val operators = typeRegistry<Operator>("operators")

    val nbtConfigs = typeRegistry<NBTTagConfig>("nbt_configs")
    
    val dependencies = typeRegistry<Dependency>("compat/dependencies")

    val itemStackConfigOverrides = typeRegistry<ItemStackConfig.Override>("items/config/overrides")

    val itemStackIdentifiers = typeRegistry<ItemStackIdentifier>("items/identifiers/types")

    val itemStackIdentifierParsers = registry<ItemStackIdentifier.Parser<*>>("items/identifiers/parser")

    private fun <T> typeRegistry(registryKey: String): RegistryReference<Class<out T>> {
        return registry<Class<out T>>(registryKey)
    }

    private fun <T> registry(registryKey: String): RegistryReference<T> {
        return RegistryKey.of<T>(root, Key.scafall(registryKey)).reference()
    }

}