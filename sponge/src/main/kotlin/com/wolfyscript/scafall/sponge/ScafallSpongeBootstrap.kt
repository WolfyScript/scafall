package com.wolfyscript.scafall.sponge

import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.loader.PluginBootstrap
import com.wolfyscript.scafall.sponge.api.ScafallSponge
import com.wolfyscript.scafall.sponge.api.SpongePluginWrapper
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.SpongeItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.spongepowered.plugin.PluginContainer
import java.util.function.Consumer

class ScafallSpongeBootstrap(applyScafall: Consumer<Scafall>, val classLoader: ClassLoader, plugin: PluginContainer) : PluginBootstrap {

    internal val corePlugin: PluginWrapper = SpongePluginWrapper(plugin)
    private val api: ScafallSponge = ScafallSponge(this)

    init {
        applyScafall.accept(api)
    }

    override fun onLoad() {
        api.load()

        val module = SimpleModule()
        module.addAbstractTypeMapping(ItemStackConfig::class.java, SpongeItemStackConfig::class.java)
    }

    override fun onEnable() {
        api.enable()
    }

    override fun onUnload() {
        api.unload()
    }
}