package com.wolfyscript.scafall.spigotlike.compat.mythicmobs

import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import org.bukkit.event.Listener

@PluginDependency("MythicMobs", MythicMobsDependency.ID)
class MythicMobsDependency : Dependency, Listener {

    companion object {
        const val ID = "mythicmobs"
        val key = Key.defaultKey(ID)
    }

    init {
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(key, MythicMobsStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(key, MythicMobsStackIdentifierParser())
        }
    }

    override var isInitialized: Boolean = true

}