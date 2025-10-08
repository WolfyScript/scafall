package com.wolfyscript.scafall.paper

import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.loader.ScafallLoader
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class PaperLoaderPlugin : JavaPlugin() {

    val bootstrap = ScafallLoader.loadObject(
        ScafallBootstrap::class.java,
        classLoader,
        "com.wolfyscript.scafall.InternalBootstrap"
    )
    private val scafall: ScafallPaper = bootstrap.loadModule {
        ScafallPaper(this.classLoader, this)
    }

    override fun onLoad() {
        scafall.initServer(Bukkit.getServer())
    }

    override fun onEnable() {
        scafall.pluginDependencyLoader.loadDependencies()
        Bukkit.getPluginManager().registerEvents(scafall.pluginDependencyLoader, this)

        scafall.server?.onLoad()
    }

    override fun onDisable() {
        scafall.server?.onUnload()
    }
}