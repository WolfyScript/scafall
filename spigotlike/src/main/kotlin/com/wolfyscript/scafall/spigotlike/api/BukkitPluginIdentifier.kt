package com.wolfyscript.scafall.spigotlike.api

import com.wolfyscript.scafall.core.ModIdentifier
import com.wolfyscript.scafall.platform.into
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class BukkitPluginIdentifier(plugin: Plugin) : ModIdentifier {

    override val id: String = plugin.name

    override fun equals(other: Any?) : Boolean {
        if (other !is BukkitPluginIdentifier) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        val result = id.hashCode()
        return result
    }

}

fun Plugin.identifier() : BukkitPluginIdentifier {
    return BukkitPluginIdentifier(this)
}

fun ModIdentifier.plugin() : Plugin? {
    return Bukkit.getPluginManager().getPlugin(this.id)
}

fun ModIdentifier.into() : BukkitPluginIdentifier {
    return into<BukkitPluginIdentifier>()
}