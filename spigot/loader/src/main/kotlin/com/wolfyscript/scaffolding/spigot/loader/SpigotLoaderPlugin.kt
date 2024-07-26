package com.wolfyscript.scaffolding.spigot.loader

import com.wolfyscript.scaffolding.common.api.classloader.InnerJarClassLoader
import com.wolfyscript.scaffolding.common.bootstrap.PluginBootstrap
import org.bukkit.plugin.java.JavaPlugin

class SpigotLoaderPlugin : JavaPlugin() {

    private val plugin: PluginBootstrap

    init {
        val classLoader = InnerJarClassLoader(javaClass.classLoader, "scaffolding-spigot.innerjar")
        plugin = classLoader.loadPlugin("com.wolfyscript.scaffolding.spigot.ScaffoldingSpigot", this)
    }

    override fun onLoad() = plugin.onLoad()

    override fun onEnable() = plugin.onEnable()

    override fun onDisable() = plugin.onUnload()

}