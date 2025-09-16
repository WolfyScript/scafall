package com.wolfyscript.scafall.spigotlike.compat.denizen

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.api.into
import com.wolfyscript.scafall.spigotlike.compat.PluginDependency
import org.bukkit.Bukkit
import org.bukkit.event.Listener

@PluginDependency("Denizen", DenizenDependency.ID)
class DenizenDependency : Dependency, Listener {

    companion object {
        const val ID = "denizen"
        val key = Key.defaultKey(ID)
    }

    override var isInitialized: Boolean = false

    init {
        Bukkit.getPluginManager().registerEvents(this, ScafallProvider.get().modInfo.into().plugin)
        ScafallRegistryTypes.itemStackIdentifiers.resolveOrThrow().apply {
            register(DenizenDependency.key, DenizenStackIdentifier::class.java)
        }
        ScafallRegistryTypes.itemStackIdentifierParsers.resolveOrThrow().apply {
            register(DenizenDependency.key, DenizenStackIdentifierParser())
        }

    }

}