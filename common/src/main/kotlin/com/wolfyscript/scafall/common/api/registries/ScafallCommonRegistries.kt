package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.items.VanillaItemStackIdentifierImpl
import com.wolfyscript.scafall.config.jackson.registerTypeRegistry
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.items.ItemStackIdentifiers
import com.wolfyscript.scafall.registry.*

class ScafallCommonRegistries(val scafall: Scafall) : ScafallRegistries {

    private lateinit var rootRegistry: Registry<Registry<*>>

    fun initRegistries() {
        rootRegistry = RegistrySimple(ScafallRegistryTypes.root)
        createRegistry(ScafallRegistryTypes.dependencies) { TypeRegistrySimple(it) }

        createRegistry(ScafallRegistryTypes.itemStackIdentifiers) {
            TypeRegistrySimple<ItemStackIdentifier>(it).apply {
                register(ItemStackIdentifiers.vanilla.key.key, VanillaItemStackIdentifierImpl::class.java)
            }
        }

        createRegistry(ScafallRegistryTypes.itemStackConfigOverrides) { RegistrySimple(it) }
        createRegistry(ScafallRegistryTypes.itemStackIdentifierParsers) {
            RegistrySimple<ItemStackIdentifier.Parser<*>>(it).apply {
                register(ItemStackIdentifiers.Parsers.vanilla.key.key, VanillaItemStackIdentifierImpl.Parser())
            }
        }
        createRegistry(ScafallRegistryTypes.valueProviders) {
            RegistrySimple<Class<out ValueProvider<*>>>(it)
        }
        createRegistry(ScafallRegistryTypes.nbtConfigs) { RegistrySimple(it) }
        createRegistry(ScafallRegistryTypes.operators) { RegistrySimple(it) }
    }

    fun <T> createRegistry(type: RegistryReference<T>, loader: (key: Key) -> Registry<T>) {
        val registry = loader(type.key.registry)
        rootRegistry.register(type.key.registry, registry)
    }

    fun registerForJackson() {
        registerTypeRegistry(
            ItemStackIdentifier::class.java,
            get(ScafallRegistryTypes.itemStackIdentifiers.key).getOrThrow()
        )
    }

    override fun <T> get(type: RegistryKey<T>): Result<Registry<T>> {
        val registry = rootRegistry[type.registry]
            ?: return Result.failure(IllegalArgumentException("No registry found for ${type.registry}"))
        return Result.success(registry as Registry<T>)
    }
}