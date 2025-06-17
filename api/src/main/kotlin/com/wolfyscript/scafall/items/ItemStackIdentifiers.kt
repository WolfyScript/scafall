package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.ValueReference
import com.wolfyscript.scafall.registry.ScafallRegistryTypes

/**
 * Contains the [ItemStackIdentifiers][ItemStackIdentifier] that are available across all platforms.
 */
object ItemStackIdentifiers {

    val vanilla = create("vanilla")

    private fun create(key: String) : ValueReference<Class<out ItemStackIdentifier>> {
        return ValueKey.of(ScafallRegistryTypes.itemStackIdentifiers.key, Key.key(Key.SCAFFOLDING_NAMESPACE, key)).reference()
    }

}