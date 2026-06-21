package com.wolfyscript.scafall.spigotlike.api

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigotlike.ScafallSpigotLike
import org.bukkit.plugin.Plugin

fun Scafall.plugin() : Plugin {
    return (this as ScafallSpigotLike).plugin
}
