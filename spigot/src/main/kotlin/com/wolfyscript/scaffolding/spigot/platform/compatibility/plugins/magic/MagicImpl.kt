/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.magic

import com.elmakers.mine.bukkit.api.event.LoadEvent
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.spigot.api.into
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scaffolding.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scaffolding.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scaffolding.spigot.platform.stackIdentifiers
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

@WUPluginIntegration(pluginName = MagicImpl.PLUGIN_NAME)
class MagicImpl protected constructor(core: Scaffolding) : PluginIntegrationAbstract(core, PLUGIN_NAME), Listener {

    override fun init(plugin: Plugin?) {
        core.registries.stackIdentifierParsers.register(MagicStackIdentifierImpl.Parser(MagicStackIdentifierImpl.getMagicAPI()))
        core.registries.stackIdentifiers.register(MagicStackIdentifierImpl::class.java)
        Bukkit.getPluginManager().registerEvents(this, core.corePlugin.into().plugin)
    }

    override fun hasAsyncLoading(): Boolean {
        return true
    }

    @EventHandler
    fun onComplete(event: LoadEvent) {
        if (event.controller != null) { //Makes sure to only mark as done when Magic will actually be enabled!
            enable()
        } else {
            ignore()
        }
    }

    companion object {
        const val PLUGIN_NAME: String = "Magic"
    }
}
