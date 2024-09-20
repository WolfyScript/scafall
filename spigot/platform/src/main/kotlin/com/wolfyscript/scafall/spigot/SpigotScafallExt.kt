package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.into
import com.wolfyscript.scafall.loader.ScafallLoader
import com.wolfyscript.scafall.spigot.api.SpigotPluginWrapper
import org.bukkit.plugin.java.JavaPlugin

fun Scafall.Companion.init(plugin: JavaPlugin, apiClassLoader: ClassLoader = this::class.java.classLoader) : Scafall {
    if (ScafallProvider.registered()) {
        return ScafallProvider.get()
    }
    ScafallLoader.initAPIClassLoader(apiClassLoader)
    val bootstrap = ScafallLoader.loadBootstrap(apiClassLoader)
    val pluginBootstrap = bootstrap.initScaffoldingPlatform(ScafallSpigotBootstrap::class.qualifiedName, JavaPlugin::class.java, plugin)
    pluginBootstrap.onLoad()
    pluginBootstrap.onEnable()

    return ScafallProvider.get()
}

fun PluginWrapper.spigot() : SpigotPluginWrapper {
    return into()
}
