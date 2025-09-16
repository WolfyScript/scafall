package com.wolfyscript.scafall.paper

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.loader.ScafallLoader
import com.wolfyscript.scafall.loader.module.Module
import org.bukkit.plugin.java.JavaPlugin

class PaperLoaderPlugin : JavaPlugin() {
    private val module: Module<Scafall>

    init {
        val bootstrap = ScafallLoader.loadObject(
            ScafallBootstrap::class.java,
            classLoader,
            "com.wolfyscript.scafall.InternalBootstrap"
        )
        module = bootstrap.loadModule {
            ScafallPaper(this.classLoader, this)
        }
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