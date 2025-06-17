package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.items.VanillaItemStackIdentifier
import com.wolfyscript.scafall.config.jackson.RegistryKeyTypeIdResolver
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.items.ItemStackIdentifiers
import com.wolfyscript.scafall.registry.*

class ScafallCommonRegistries(val scafall: Scafall) : ScafallRegistries {

    private val rootRegistry = RegistrySimple<Registry<*>>(ScafallRegistryTypes.root)

    fun initRegistries() {

        createRegistry(ScafallRegistryTypes.itemStackIdentifiers) {
            TypeRegistrySimple<ItemStackIdentifier>(it).apply {
                register(ItemStackIdentifiers.vanilla.key.key, VanillaItemStackIdentifier::class.java)
            }
        }
        createRegistry(ScafallRegistryTypes.itemStackConfigOverrides) { RegistrySimple(it) }
        createRegistry(ScafallRegistryTypes.itemStackIdentifierParsers) { RegistrySimple(it) }
        createRegistry(ScafallRegistryTypes.valueProviders) { RegistrySimple(it) }
        createRegistry(ScafallRegistryTypes.nbtConfigs) { RegistrySimple(it) }
        createRegistry(ScafallRegistryTypes.operators) { RegistrySimple(it) }
    }

    fun <T> createRegistry(type: RegistryReference<T>, loader: (key: Key) -> Registry<T>) {
        val registry = loader(type.key.registry)
        rootRegistry.register(type.key.registry, registry)
    }

    fun registerForJackson() {
        // TODO
        RegistryKeyTypeIdResolver.registerTypeRegistry(
            ItemStackIdentifier::class.java,
            get(ScafallRegistryTypes.itemStackIdentifiers.key).getOrThrow()
        )

    }

    override fun <T> get(type: RegistryKey<T>): Result<Registry<T>> {
        val registry = rootRegistry[type.registry]
        if (registry == null) {
            return Result.failure(IllegalArgumentException("No registry found for ${type.registry}"))
        }
        return Result.success(registry as Registry<T>)
    }
}