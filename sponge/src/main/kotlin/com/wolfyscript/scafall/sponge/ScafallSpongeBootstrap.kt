package com.wolfyscript.scafall.sponge

import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.sponge.api.ScafallSponge
import com.wolfyscript.scafall.sponge.api.SpongePluginWrapper
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.spongepowered.plugin.PluginContainer
import java.util.function.Consumer

class ScafallSpongeBootstrap(applyScafall: Consumer<Scafall>, val classLoader: ClassLoader, plugin: PluginContainer) : ScafallBootstrap.ScafallModule {

    internal val corePlugin: ModWrapper = SpongePluginWrapper(plugin)
    override val bridge: ScafallSponge = ScafallSponge(this)

    init {
        applyScafall.accept(bridge)
    }

    override fun onLoad() {
        bridge.load()

        val module = SimpleModule()
        module.addAbstractTypeMapping(ItemStackConfig::class.java, SpongeItemStackConfig::class.java)
    }

    override fun onEnable() {
        bridge.enable()
    }

    override fun onUnload() {
        bridge.unload()
    }

}