package com.wolfyscript.scafall.spigotlike.api

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.common.api.into
import org.bukkit.plugin.Plugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BukkitPluginWrapper(val plugin: Plugin) : ModWrapper {

    override val name: String = plugin.name
    override val logger: Logger
        get() = LoggerFactory.getLogger(plugin.logger.name)

}

fun ModWrapper.into() : BukkitPluginWrapper {
    return into<BukkitPluginWrapper>()
}