package com.wolfyscript.scafall.sponge

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.loader.ScafallLoader
import org.spongepowered.plugin.PluginContainer

fun Scafall.Companion.init(plugin: PluginContainer, apiClassLoader: ClassLoader = this::class.java.classLoader) : Scafall {
    if (ScafallProvider.registered()) {
        return ScafallProvider.get()
    }
    ScafallLoader.initAPIClassLoader(apiClassLoader)
    val bootstrap = ScafallLoader.loadBootstrap(apiClassLoader)
    val pluginBootstrap = bootstrap.initScaffoldingPlatform(ScafallSpongeBootstrap::class.qualifiedName, PluginContainer::class.java, plugin)
    pluginBootstrap.onLoad()
    pluginBootstrap.onEnable()
    return ScafallProvider.get()
}
