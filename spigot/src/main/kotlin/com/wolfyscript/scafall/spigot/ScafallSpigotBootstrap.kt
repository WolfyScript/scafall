package com.wolfyscript.scafall.spigot

import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.inject.Inject
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.spigot.api.ScafallSpigot
import com.wolfyscript.scafall.spigot.api.SpigotPluginWrapper
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStackConfig
import com.wolfyscript.scafall.spigot.platform.registerSpigotPlatform
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.bukkit.plugin.java.JavaPlugin

class ScafallSpigotBootstrap @Inject constructor(val classLoader: ClassLoader, plugin: JavaPlugin) : ScafallBootstrap.ScafallModule {

    internal val corePlugin: SpigotPluginWrapper = SpigotPluginWrapper(plugin)
    override val bridge: ScafallSpigot = ScafallSpigot(this)

    override fun onLoad() {
        bridge.load()

        // Register platform specific registries
        bridge.registries.registerSpigotPlatform()

        val module = SimpleModule()
        module.addAbstractTypeMapping(ItemStackConfig::class.java, BukkitItemStackConfig::class.java)
    }

    override fun onEnable() {
        bridge.enable()
    }

    override fun onUnload() {
        bridge.unload()
    }

}