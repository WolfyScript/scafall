package com.wolfyscript.scafall.spigotlike.compat.denizen

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

@PluginDependency(DenizenDependency.PLUGIN_NAME, DenizenDependency.ID)
class DenizenDependency : Dependency, Listener {

    companion object {
        const val PLUGIN_NAME = "Denizen"
        const val ID = "denizen"
        val key = Key.defaultKey(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
    }

    override fun onInit() {
        isInitialized = true
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(DenizenDependency.key, DenizenStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(DenizenDependency.key, DenizenStackIdentifierParser())
        }
    }

    @EventHandler
    private fun onEnable(event: PluginEnableEvent) {
        if (event.plugin.name == PLUGIN_NAME) {
            ScafallProvider.get().dependencyManager.initiateDependency(key)
        }
    }

}