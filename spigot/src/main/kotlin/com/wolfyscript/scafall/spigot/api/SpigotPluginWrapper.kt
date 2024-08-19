package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.common.api.into
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SpigotPluginWrapper(val plugin: Plugin) : PluginWrapper {

    override val name: String = plugin.name
    override val logger: Logger
        get() = LoggerFactory.getLogger(plugin.logger.name)

}

internal fun PluginWrapper.into() : SpigotPluginWrapper {
    return into<SpigotPluginWrapper>()
}