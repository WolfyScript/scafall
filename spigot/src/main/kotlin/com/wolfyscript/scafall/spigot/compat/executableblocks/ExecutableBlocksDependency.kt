package com.wolfyscript.scafall.spigot.compat.executableblocks

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigot.api.into
import com.wolfyscript.scafall.spigot.compat.PluginDependency
import org.bukkit.Bukkit
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
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(ExecutableBlocksDependency.key, ExecutableBlocksStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(ExecutableBlocksDependency.key, ExecutableBlocksStackIdentifierParser())
        }

    }

}