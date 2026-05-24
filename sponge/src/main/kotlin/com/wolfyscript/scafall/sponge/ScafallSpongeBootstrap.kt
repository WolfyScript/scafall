package com.wolfyscript.scafall.sponge

import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.core.ModIdentifier
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.sponge.api.ScafallSponge
import com.wolfyscript.scafall.sponge.api.SpongePluginIdentifier
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.slf4j.LoggerFactory
import org.spongepowered.plugin.PluginContainer
import java.util.function.Consumer

class ScafallSpongeBootstrap(applyScafall: Consumer<Scafall>, val classLoader: ClassLoader, plugin: PluginContainer) : ScafallBootstrap(classLoader) {

    internal val corePlugin: ModIdentifier = SpongePluginIdentifier(plugin)
    val bridge: ScafallSponge = ScafallSponge(this, LoggerFactory.getLogger(plugin.logger().name))

    init {
        applyScafall.accept(bridge)
    }

    fun onLoad() {
        bridge.load()

        val module = SimpleModule()
        module.addAbstractTypeMapping(ItemStackConfig::class.java, SpongeItemStackConfig::class.java)
    }

    fun onEnable() {
//        bridge.enable()
    }

    fun onUnload() {
//        bridge.unload()
    }

}