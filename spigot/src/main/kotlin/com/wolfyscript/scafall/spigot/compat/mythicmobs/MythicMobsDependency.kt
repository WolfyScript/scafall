package com.wolfyscript.scafall.spigot.compat.mythicmobs

import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.compat.PluginDependency
import org.bukkit.event.Listener

@PluginDependency("MythicMobs", MythicMobsDependency.ID)
class MythicMobsDependency : Dependency, Listener {

    companion object {
        const val ID = "mythicmobs"
        val key = Key.defaultKey(ID)
    }

    override var isInitialized: Boolean = true

}