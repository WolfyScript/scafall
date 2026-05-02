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

    val valueProviders = createRegistryType<Class<out ValueProvider<*>>>("value_providers")

    val operators = createRegistryType<Class<out Operator>>("operators")

    val nbtConfigs = createRegistryType<Class<out NBTTagConfig>>("nbt_configs")
    
    val dependencies = createRegistryType<Class<out Dependency>>("compat/dependencies")

    val itemStackConfigOverrides = createRegistryType<Class<out ItemStackConfig.Override>>("items/config/overrides")

    val itemStackIdentifiers = createRegistryType<Class<out ItemStackIdentifier>>("items/identifiers/types")

    val itemStackIdentifierParsers = createRegistryType<ItemStackIdentifier.Parser<*>>("items/identifiers/parser")

    private fun <T> createRegistryType(registryKey: String): RegistryReference<T> {
        return RegistryKey.of<T>(root, Key.scafall(registryKey)).reference()
    }

}