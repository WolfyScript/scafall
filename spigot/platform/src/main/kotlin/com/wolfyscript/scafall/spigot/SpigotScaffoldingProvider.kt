package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.loader.ScaffoldingLoader
import org.bukkit.plugin.java.JavaPlugin

fun Scafall.Companion.initOnSpigot(plugin: JavaPlugin, apiClassLoader: ClassLoader = this::class.java.classLoader) : Scafall {
    if (ScafallProvider.registered()) {
        return ScafallProvider.get()
    }
    ScaffoldingLoader.initAPIClassLoader(apiClassLoader)
    val bootstrap = ScaffoldingLoader.loadBootstrap(apiClassLoader)
    val pluginBootstrap = bootstrap.initScaffoldingPlatform("com.wolfyscript.scafall.spigot.ScaffoldingSpigotBootstrap", JavaPlugin::class.java, plugin)
    pluginBootstrap.onEnable()

    return ScafallProvider.get()
}
