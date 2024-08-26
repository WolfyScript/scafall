package com.wolfyscript.scafall.spigot

import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.loader.PluginBootstrap
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.api.ScafallSpigot
import com.wolfyscript.scafall.spigot.api.SpigotPluginWrapper
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStackConfig
import com.wolfyscript.scafall.spigot.platform.registerSpigotPlatform
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

internal class ScaffoldingSpigotBootstrap(applyScafall: Consumer<Scafall>, plugin: JavaPlugin) : PluginBootstrap {

    internal val corePlugin: SpigotPluginWrapper = SpigotPluginWrapper(plugin)
    private val api: ScafallSpigot = ScafallSpigot(this)

    init {
        applyScafall.accept(api)
    }

    override fun onLoad() {
        api.load()

        // Register platform specific registries
        api.registries.registerSpigotPlatform()

        val module = SimpleModule()
        module.addAbstractTypeMapping(ItemStackConfig::class.java, BukkitItemStackConfig::class.java)
    }

    override fun onEnable() {
        api.enable()
    }

    override fun onUnload() {
        api.unload()
    }
}