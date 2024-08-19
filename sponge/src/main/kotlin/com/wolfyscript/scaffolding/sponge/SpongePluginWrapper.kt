package com.wolfyscript.scafall.sponge

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.common.api.into
import org.spongepowered.plugin.PluginContainer

class SpongePluginWrapper(val plugin: PluginContainer) : PluginWrapper {

    override val name: String = plugin.metadata().name().orElse("")

}

internal fun PluginWrapper.into() : SpongePluginWrapper {
    return into<SpongePluginWrapper>()
}
