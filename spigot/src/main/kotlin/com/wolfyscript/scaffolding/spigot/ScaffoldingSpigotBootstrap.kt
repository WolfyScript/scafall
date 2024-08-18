package com.wolfyscript.scaffolding.spigot

import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scaffolding.loader.PluginBootstrap
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.spigot.api.ScaffoldingSpigot
import com.wolfyscript.scaffolding.spigot.api.SpigotPluginWrapper
import com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.BukkitItemStackConfig
import com.wolfyscript.scaffolding.spigot.platform.registerSpigotPlatform
import com.wolfyscript.scaffolding.wrappers.world.items.ItemStackConfig
import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

internal class ScaffoldingSpigotBootstrap(applyScaffolding: Consumer<Scaffolding>, plugin: JavaPlugin) : PluginBootstrap {

    private val api: ScaffoldingSpigot = ScaffoldingSpigot(this)
    internal val corePlugin: SpigotPluginWrapper = SpigotPluginWrapper(plugin)

    init {
        applyScaffolding.accept(api)
    }

    override fun onLoad() {
        val module = SimpleModule()

        module.addAbstractTypeMapping(ItemStackConfig::class.java, BukkitItemStackConfig::class.java)

        // Register platform specific registries
        api.registries.registerSpigotPlatform()
    }

    override fun onEnable() {
        api.enable()
    }

    override fun onUnload() {

    }
}