package com.wolfyscript.scaffolding.loader.spigot

import com.wolfyscript.scaffolding.loader.ScaffoldingLoader
import com.wolfyscript.scaffolding.loader.InnerJarClassloader
import com.wolfyscript.scaffolding.loader.PluginBootstrap
import org.bukkit.plugin.java.JavaPlugin

class SpigotLoaderPlugin : JavaPlugin() {

    private val plugin: PluginBootstrap

    init {
        ScaffoldingLoader.initAPIClassLoader(InnerJarClassloader.create(javaClass.classLoader, "scaffolding-api.innerjar"))
        val bootstrap = ScaffoldingLoader.loadScaffoldingBootstrap("scaffolding-spigot.innerjar")
        plugin = bootstrap.initScaffoldingPlatform("com.wolfyscript.scaffolding.spigot.ScaffoldingSpigotBootstrap", this)
    }

    override fun onLoad() = plugin.onLoad()

    override fun onEnable() = plugin.onEnable()

    override fun onDisable() = plugin.onUnload()

}