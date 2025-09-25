package com.wolfyscript.scafall.spigotlike.compat.eco

import com.willfp.eco.core.EcoPlugin
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

@PluginDependency(EcoDependency.PLUGIN_NAME, EcoDependency.ID)
class EcoDependency : Dependency, Listener {

    companion object {
        const val ID = "eco"
        const val PLUGIN_NAME = ID
        val key = Key.scafall(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
    }

    override fun onInit() {
        isInitialized = true
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(EcoDependency.key, EcoStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(EcoDependency.key, EcoStackIdentifierParser())
        }
    }

    @EventHandler
    private fun onEnabled(event: PluginEnableEvent) {
        val plugin = event.plugin
        if (plugin.name == PLUGIN_NAME && plugin is EcoPlugin) {
            plugin.afterLoad {
                ScafallProvider.get().dependencyManager.initiateDependency(key)
            }
        }
    }

}