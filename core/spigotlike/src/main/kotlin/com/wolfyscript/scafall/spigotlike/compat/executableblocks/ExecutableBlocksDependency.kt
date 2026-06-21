package com.wolfyscript.scafall.spigotlike.compat.executableblocks

import com.ssomar.executableblocks.api.load.ExecutableBlocksPostLoadEvent
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.plugin
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("ExecutableBlocks", ExecutableBlocksDependency.ID)
class ExecutableBlocksDependency : Dependency, Listener {

    companion object {
        const val ID = "executableblocks"
        val key = Key.scafall(ID)
    }

    override var isInitialized: Boolean = false

    init {
        ScafallProvider.get().plugin().let { Bukkit.getPluginManager().registerEvents(this, it) }
    }

    override fun onInit() {
        isInitialized = true
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(ExecutableBlocksDependency.key, ExecutableBlocksStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(ExecutableBlocksDependency.key, ExecutableBlocksStackIdentifierParser())
        }
    }

    @EventHandler
    private fun onLoaded(event: ExecutableBlocksPostLoadEvent) {
        ScafallProvider.get().dependencyManager.initiateDependency(key)
    }

}