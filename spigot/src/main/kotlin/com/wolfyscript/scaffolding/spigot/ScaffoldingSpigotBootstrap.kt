package com.wolfyscript.scaffolding.spigot

import com.wolfyscript.scaffolding.common.bootstrap.PluginBootstrap
import com.wolfyscript.scaffolding.spigot.api.ScaffoldingSpigot
import org.bukkit.plugin.java.JavaPlugin

internal class ScaffoldingSpigotBootstrap(internal val plugin: JavaPlugin) : PluginBootstrap {

    private val api: ScaffoldingSpigot = ScaffoldingSpigot(this)

    override fun onLoad() {

    }

    override fun onEnable() {
        api.enable()
    }

    override fun onUnload() {

    }
}