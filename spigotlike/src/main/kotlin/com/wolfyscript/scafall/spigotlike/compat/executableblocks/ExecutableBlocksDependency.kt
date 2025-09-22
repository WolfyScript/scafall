package com.wolfyscript.scafall.spigotlike.compat.executableblocks

import com.ssomar.executableblocks.api.load.ExecutableBlocksPostLoadEvent
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.into
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@PluginDependency("ExecutableBlocks", ExecutableBlocksDependency.ID)
class ExecutableBlocksDependency : Dependency, Listener {

    companion object {
        const val ID = "executableblocks"
        val key = Key.defaultKey(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
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