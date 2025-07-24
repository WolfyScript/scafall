package com.wolfyscript.scafall.spigot.compat.eco

import com.willfp.eco.core.EcoPlugin
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigot.api.into
import com.wolfyscript.scafall.spigot.compat.PluginDependency
import org.bukkit.Bukkit
import org.bukkit.event.Listener

@PluginDependency("eco", EcoDependency.ID)
class EcoDependency : Dependency, Listener {

    companion object {
        const val ID = "eco"
        val key = Key.defaultKey(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(EcoDependency.key, EcoStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(EcoDependency.key, EcoStackIdentifierParser())
        }

        val plugin = EcoPlugin.getPlugin("eco")
        plugin?.afterLoad {
            isInitialized = true
        }

    }

}