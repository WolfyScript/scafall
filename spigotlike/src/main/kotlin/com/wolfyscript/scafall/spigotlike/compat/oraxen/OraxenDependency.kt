package com.wolfyscript.scafall.spigotlike.compat.oraxen

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.into
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import io.th0rgal.oraxen.api.events.OraxenItemsLoadedEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("Oraxen", OraxenDependency.ID)
class OraxenDependency : Dependency, Listener {

    companion object {
        const val ID = "oraxen"
        val key = Key.defaultKey(ID)
    }

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(key, OraxenItemStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(key, OraxenStackIdentifierParser())
        }
    }

    override var isInitialized: Boolean = false

    @EventHandler
    private fun onItemsLoaded(event: OraxenItemsLoadedEvent) {
        isInitialized = true
        ScafallProvider.get().dependencyManager.initiateDependency(key)
    }

}