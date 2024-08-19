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
package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.util

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.Flags
import com.sk89q.worldguard.protection.flags.StateFlag
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

object WGUtils {
    var inst: WorldGuardPlugin = WorldGuardPlugin.inst()

    fun teleportEntity(entity: Entity, location: Location, player: Player): Boolean {
        if (hasPermBuild(location, player)) {
            entity.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN)
            return true
        }
        return false
    }

    fun hasPermBuild(location: Location, player: Player): Boolean {
        return hasPermBuild(location, player, Flags.BUILD)
    }

    fun hasPermBuild(location: Location, player: Player, vararg flag: StateFlag?): Boolean {
        val loc = BukkitAdapter.adapt(location)
        val container = WorldGuard.getInstance().platform.regionContainer
        val query = container.createQuery()
        val set = query.getApplicableRegions(loc)
        val localPlayer = inst.wrapPlayer(player)
        return set.testState(localPlayer, *flag) || hasBypassPerm(player, location)
    }

    fun hasBypassPerm(player: Player, location: Location): Boolean {
        return hasBypassPerm(player, location.world)
    }

    fun hasBypassPerm(player: Player, world: World): Boolean {
        return player.hasPermission("worldguard.region.bypass.*") || player.hasPermission("worldguard.region.bypass." + world.name)
    }
}
