package com.wolfyscript.scafall.spigot.compat.mmoitems

import com.wolfyscript.scafall.compat.Dependency
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.compat.PluginDependency
import org.bukkit.event.Listener

@PluginDependency("MMOItems", MMOItemsDependency.ID)
class MMOItemsDependency : Dependency, Listener {

    companion object {
        const val ID = "mmoitems"
        val key = Key.defaultKey(ID)
    }

    override var isInitialized: Boolean = true

}