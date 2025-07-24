package com.wolfyscript.scafall.spigot.compat.itemsadder

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigot.api.into
import com.wolfyscript.scafall.spigot.compat.PluginDependency
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("ItemsAdder", ItemsAdderDependency.ID)
class ItemsAdderDependency : Dependency, Listener {

    companion object {
        const val ID = "itemsadder"
        val key = Key.defaultKey(ID)
    }

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(ItemsAdderDependency.key, ItemsAdderStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(ItemsAdderDependency.key, ItemsAdderStackIdentifierParser())
        }
    }

    override var isInitialized: Boolean = false

    @EventHandler
    private fun onInit(event: ItemsAdderLoadDataEvent) {
        isInitialized = true
        ScafallProvider.get().dependencyManager.initiateDependency(key)
    }

}