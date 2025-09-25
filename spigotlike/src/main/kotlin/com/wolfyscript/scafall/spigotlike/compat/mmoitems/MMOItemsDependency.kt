package com.wolfyscript.scafall.spigotlike.compat.mmoitems

import com.elmakers.mine.bukkit.api.event.LoadEvent
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.into
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("MMOItems", MMOItemsDependency.ID)
class MMOItemsDependency : Dependency, Listener {

    companion object {
        const val ID = "mmoitems"
        val key = Key.scafall(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
    }

    override fun onInit() {
        isInitialized = true
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(key, MMOItemsStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(key, MMOItemsStackIdentifierParser())
        }
    }

    @EventHandler
    private fun onLoaded(event: LoadEvent) {
        if (event.controller != null) {
            ScafallProvider.get().dependencyManager.initiateDependency(key)
        }
    }

}