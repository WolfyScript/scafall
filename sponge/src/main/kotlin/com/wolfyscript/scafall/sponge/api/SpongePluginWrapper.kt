package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.platform.into
import org.slf4j.LoggerFactory
import org.spongepowered.plugin.PluginContainer

class SpongePluginWrapper(val plugin: PluginContainer) : ModWrapper {

    override val name: String = plugin.metadata().name().orElse("")

    override val logger: org.slf4j.Logger
        get() = LoggerFactory.getLogger(plugin.logger().name)

}

internal fun ModWrapper.into() : SpongePluginWrapper {
    return into<SpongePluginWrapper>()
}
