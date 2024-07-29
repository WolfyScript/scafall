package com.wolfyscript.scaffolding.sponge

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.common.api.into
import org.spongepowered.plugin.PluginContainer

class SpongePluginWrapper(val plugin: PluginContainer) : PluginWrapper {

    override val name: String = plugin.metadata().name().orElse("")

}

internal fun PluginWrapper.into() : SpongePluginWrapper {
    return into<SpongePluginWrapper>()
}
