package com.wolfyscript.scaffolding.spigot

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.loader.ScaffoldingLoader
import org.bukkit.plugin.java.JavaPlugin

fun Scaffolding.Companion.initOnSpigot(plugin: JavaPlugin, apiClassLoader: ClassLoader = this::class.java.classLoader) : Scaffolding {
    if (ScaffoldingProvider.registered()) {
        return ScaffoldingProvider.get()
    }
    ScaffoldingLoader.initAPIClassLoader(apiClassLoader)
    val bootstrap = ScaffoldingLoader.loadScaffoldingBootstrap("scaffolding-spigot.innerjar")
    bootstrap.initScaffoldingPlatform("com.wolfyscript.scaffolding.spigot.ScaffoldingSpigotBootstrap", plugin)

    return ScaffoldingProvider.get()
}
