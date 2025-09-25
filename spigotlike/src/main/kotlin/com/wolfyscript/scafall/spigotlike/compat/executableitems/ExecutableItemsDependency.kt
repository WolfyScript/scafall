package com.wolfyscript.scafall.spigotlike.compat.executableitems

import com.ssomar.score.api.executableitems.load.ExecutableItemsPostLoadEvent
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.into
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("ExecutableItems", ExecutableItemsDependency.ID)
class ExecutableItemsDependency : Dependency, Listener {

    companion object {
        const val ID = "executableitems"
        val key = Key.scafall(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
    }

    override fun onInit() {
        isInitialized = true
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(ExecutableItemsDependency.key, ExecutableItemsStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(ExecutableItemsDependency.key, ExecutableItemsStackIdentifierParser())
        }
    }

    @EventHandler
    private fun onLoaded(event: ExecutableItemsPostLoadEvent) {
        ScafallProvider.get().dependencyManager.initiateDependency(key)
    }

}