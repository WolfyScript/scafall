package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.loader.ScafallLoader
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SpigotLoaderPlugin : JavaPlugin() {

    val bootstrap = ScafallLoader.loadObject(
        ScafallBootstrap::class.java,
        classLoader,
        "com.wolfyscript.scafall.InternalBootstrap"
    )
    private val scafall: ScafallSpigot = bootstrap.loadModule {
        ScafallSpigot(this.classLoader, this)
    }

    override fun onLoad() {
        scafall.initServer(Bukkit.getServer())
    }

    override fun onEnable() {
        scafall.pluginDependencyLoader.loadDependencies()
        Bukkit.getPluginManager().registerEvents(scafall.pluginDependencyLoader, this)

        // Trick to run our own custom scheduler on each tick
        Bukkit.getScheduler().runTaskTimer(scafall.plugin, Runnable {
            scafall.scheduler.tick(MinecraftServer.getServer().tickCount)
        }, 0L, 1L)

        scafall.server?.onLoad()
    }

    override fun onDisable() {
        scafall.server?.onUnload()
    }
}