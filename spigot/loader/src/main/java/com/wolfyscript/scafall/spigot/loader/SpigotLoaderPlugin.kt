package com.wolfyscript.scafall.spigot.loader

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.ScafallLoader.loadObject
import com.wolfyscript.scafall.loader.module.Module
import org.bukkit.plugin.java.JavaPlugin

class SpigotLoaderPlugin : JavaPlugin() {
    private val module: Module<Scafall>

    init {
        val overlayLoader = InnerJarClassloader.create("scafall-spigot-innerjar", classLoader, classLoader, "scafall-spigot.innerjar")
        val bootstrap = loadObject(
            ScafallBootstrap::class.java,
            overlayLoader,
            "com.wolfyscript.scafall.InternalBootstrap"
        )
        module = bootstrap.loadModuleFromInnerJar(
            "com.wolfyscript.scafall.spigot.ScafallSpigotBootstrap",
            JavaPlugin::class.java,
            this
        )
    }

    override fun onLoad() {
        module.onLoad()
    }

    override fun onEnable() {
        module.onEnable()
    }

    override fun onDisable() {
        module.onUnload()
    }
}
