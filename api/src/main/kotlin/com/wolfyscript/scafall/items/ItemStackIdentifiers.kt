package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ValueReference
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.registry.referenced

/**
 * Contains the [ItemStackIdentifiers][ItemStackIdentifier] that are available across all platforms.
 */
object ItemStackIdentifiers {

    val vanilla = create<Class<VanillaItemStackIdentifier>>("vanilla")

    private inline fun <reified T: Class<out ItemStackIdentifier>> create(key: String) : ValueReference<Class<out ItemStackIdentifier>, T> {
        return ScafallRegistryTypes.itemStackIdentifiers.key.referenced<Class<out ItemStackIdentifier>, T>(Key.key(Key.SCAFFOLDING_NAMESPACE, key)).reference()
    }

    /**
     * The parsers for the ItemStackIdentifiers
     */
    object Parsers {

        val vanilla = create<VanillaItemStackIdentifier.Parser>("vanilla")

        private inline fun <reified T: ItemStackIdentifier.Parser<*>> create(key: String) : ValueReference<ItemStackIdentifier.Parser<*>, T> {
            return ScafallRegistryTypes.itemStackIdentifierParsers.key.referenced<ItemStackIdentifier.Parser<*>, T>(Key.key(Key.SCAFFOLDING_NAMESPACE, key)).reference()
        }

    }

}