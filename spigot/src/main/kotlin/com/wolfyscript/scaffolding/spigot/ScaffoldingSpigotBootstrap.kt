package com.wolfyscript.scaffolding.spigot

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.common.bootstrap.PluginBootstrap
import com.wolfyscript.scaffolding.spigot.api.ScaffoldingSpigot
import com.wolfyscript.scaffolding.spigot.api.SpigotPluginWrapper
import org.bukkit.plugin.java.JavaPlugin

internal class ScaffoldingSpigotBootstrap(plugin: JavaPlugin) : PluginBootstrap {

    private val api: ScaffoldingSpigot = createAPI()
    internal val corePlugin: SpigotPluginWrapper = SpigotPluginWrapper(plugin)

    private fun createAPI() : ScaffoldingSpigot {
        val scaffolding = ScaffoldingSpigot(this)
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        ScaffoldingProvider.register(scaffolding);
        return scaffolding
    }

    override fun onLoad() {

    }

    override fun onEnable() {
        api.enable()
    }

    override fun onUnload() {

    }
}