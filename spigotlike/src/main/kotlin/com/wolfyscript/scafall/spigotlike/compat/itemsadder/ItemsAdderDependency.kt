package com.wolfyscript.scafall.spigotlike.compat.itemsadder

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.into
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("ItemsAdder", ItemsAdderDependency.ID)
class ItemsAdderDependency : Dependency, Listener {

    companion object {
        const val ID = "itemsadder"
        val key = Key.scafall(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
    }

    override fun onInit() {
        isInitialized = true
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(ItemsAdderDependency.key, ItemsAdderStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(ItemsAdderDependency.key, ItemsAdderStackIdentifierParser())
        }
    }

    @EventHandler
    private fun onInit(event: ItemsAdderLoadDataEvent) {
        ScafallProvider.get().dependencyManager.initiateDependency(key)
    }

}