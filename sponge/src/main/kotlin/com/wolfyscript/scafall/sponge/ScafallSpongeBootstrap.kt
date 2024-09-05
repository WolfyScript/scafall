package com.wolfyscript.scafall.sponge

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.loader.PluginBootstrap
import com.wolfyscript.scafall.sponge.api.ScafallSponge
import com.wolfyscript.scafall.sponge.api.SpongePluginWrapper
import org.spongepowered.plugin.PluginContainer
import java.util.function.Consumer

class ScafallSpongeBootstrap(applyScafall: Consumer<Scafall>, plugin: PluginContainer) : PluginBootstrap {

    internal val corePlugin: PluginWrapper = SpongePluginWrapper(plugin)
    private val api: ScafallSponge = ScafallSponge(this)

    init {
        applyScafall.accept(api)
    }

    override fun onLoad() {
        api.load()
    }

    override fun onEnable() {
        api.enable()
    }

    override fun onUnload() {
        api.unload()
    }
}