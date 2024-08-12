package com.wolfyscript.scaffolding.spigot

import com.wolfyscript.scaffolding.loader.PluginBootstrap
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.spigot.api.ScaffoldingSpigot
import com.wolfyscript.scaffolding.spigot.api.SpigotPluginWrapper
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

internal class ScaffoldingSpigotBootstrap(applyScaffolding: Consumer<Scaffolding>, plugin: JavaPlugin) : PluginBootstrap {

    private val api: ScaffoldingSpigot = ScaffoldingSpigot(this)
    internal val corePlugin: SpigotPluginWrapper = SpigotPluginWrapper(plugin)

    init {
        applyScaffolding.accept(api)
    }

    override fun onLoad() {

    }

    override fun onEnable() {
        api.enable()
    }

    override fun onUnload() {

    }
}