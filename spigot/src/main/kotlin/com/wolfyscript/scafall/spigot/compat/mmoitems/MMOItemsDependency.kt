package com.wolfyscript.scafall.spigot.compat.mmoitems

import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigot.compat.PluginDependency
import com.wolfyscript.scafall.spigot.compat.magic.MagicStackIdentifier
import com.wolfyscript.scafall.spigot.compat.magic.MagicStackIdentifierParser
import org.bukkit.event.Listener

@PluginDependency("MMOItems", MMOItemsDependency.ID)
class MMOItemsDependency : Dependency, Listener {

    companion object {
        const val ID = "mmoitems"
        val key = Key.defaultKey(ID)
    }

    init {
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(key, MMOItemsStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(key, MMOItemsStackIdentifierParser())
        }
    }

    override var isInitialized: Boolean = true

}