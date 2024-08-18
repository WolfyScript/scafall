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
package com.wolfyscript.scaffolding.spigot.platform.world.items.actions

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.spigot.api.compatibilityManager
import com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.PlaceholderAPIIntegration
import org.bukkit.Bukkit
import java.util.function.Consumer

class ActionCommand : Action<DataPlayer>(
    KEY,
    DataPlayer::class.java
) {
    @JsonIgnore
    private val papi: PlaceholderAPIIntegration? = get().compatibilityManager.plugins.getIntegration(PlaceholderAPIIntegration.KEY, PlaceholderAPIIntegration::class.java)
    private var playerCommands: List<String> = ArrayList()
    private var consoleCommands: List<String> = ArrayList()

    override fun execute(core: Scaffolding, data: DataPlayer) {
        val player = data.player
        var resultPlayerCmds = playerCommands
        var resultConsoleCmds = consoleCommands
        if (papi != null) {
            resultPlayerCmds = papi.setPlaceholders(player, papi.setBracketPlaceholders(player, playerCommands))
            resultConsoleCmds = papi.setPlaceholders(player, papi.setBracketPlaceholders(player, consoleCommands))
        }
        resultPlayerCmds.forEach(Consumer { command: String? ->
            player.performCommand(
                command!!
            )
        })
        resultConsoleCmds.forEach(Consumer { cmd: String? ->
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                cmd!!
            )
        })
    }

    fun getConsoleCommands(): List<String?> {
        return java.util.List.copyOf(consoleCommands)
    }

    fun setConsoleCommands(consoleCommands: List<String>) {
        this.consoleCommands = consoleCommands
    }

    fun getPlayerCommands(): List<String?> {
        return java.util.List.copyOf(playerCommands)
    }

    fun setPlayerCommands(playerCommands: List<String>) {
        this.playerCommands = playerCommands
    }

    companion object {
        val KEY: Key = scaffoldingKey("player/command")
    }
}
