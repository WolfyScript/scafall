package com.wolfyscript.scafall.spigot.compat.magic

import com.elmakers.mine.bukkit.api.event.LoadEvent
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigot.compat.PluginDependency
import com.wolfyscript.scafall.spigot.compat.itemsadder.ItemsAdderDependency
import com.wolfyscript.scafall.spigot.compat.itemsadder.ItemsAdderStackIdentifier
import com.wolfyscript.scafall.spigot.compat.itemsadder.ItemsAdderStackIdentifierParser
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("Magic", MagicDependency.ID)
class MagicDependency : Dependency, Listener {

    companion object {
        const val ID = "magic"
        val key = Key.defaultKey(ID)
    }

    init {
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(key, MagicStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(key, MagicStackIdentifierParser())
        }
    }

    override var isInitialized: Boolean = false

    @EventHandler
    private fun onLoaded(event: LoadEvent) {
        if (event.controller != null) {
            isInitialized = true
        }
    }

}