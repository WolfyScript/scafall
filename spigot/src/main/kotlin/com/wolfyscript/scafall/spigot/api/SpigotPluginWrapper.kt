package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.common.api.into
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SpigotPluginWrapper(val plugin: Plugin) : PluginWrapper {

    override val name: String = plugin.name
    override val logger: Logger
        get() = LoggerFactory.getLogger(plugin.logger.name)

}

fun PluginWrapper.into() : SpigotPluginWrapper {
    return into<SpigotPluginWrapper>()
}