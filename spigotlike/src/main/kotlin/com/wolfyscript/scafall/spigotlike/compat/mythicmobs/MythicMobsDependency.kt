package com.wolfyscript.scafall.spigotlike.compat.mythicmobs

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.into
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginEnableEvent

@PluginDependency(MythicMobsDependency.PLUGIN_NAME, MythicMobsDependency.ID)
class MythicMobsDependency : Dependency, Listener {

    companion object {
        const val PLUGIN_NAME = "MythicMobs"
        const val ID = "mythicmobs"
        val key = Key.defaultKey(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
    }

    override fun onInit() {
        isInitialized = true
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(key, MythicMobsStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(key, MythicMobsStackIdentifierParser())
        }
    }

    @EventHandler
    private fun mythicMobsEnabled(event: PluginEnableEvent) {
        if (event.plugin.name == PLUGIN_NAME) {
            ScafallProvider.get().dependencyManager.initiateDependency(key)
        }
    }

}