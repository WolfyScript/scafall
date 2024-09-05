package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.common.api.into
import org.slf4j.LoggerFactory
import org.spongepowered.plugin.PluginContainer

class SpongePluginWrapper(val plugin: PluginContainer) : PluginWrapper {

    override val name: String = plugin.metadata().name().orElse("")

    override val logger: org.slf4j.Logger
        get() = LoggerFactory.getLogger(plugin.logger().name)

}

internal fun PluginWrapper.into() : SpongePluginWrapper {
    return into<SpongePluginWrapper>()
}
