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
package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.util

import com.plotsquared.bukkit.util.BukkitUtil
import com.plotsquared.core.PlotAPI
import com.plotsquared.core.plot.Plot
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

object PSUtils {
    //PlotSquared API Utils
    private val plotAPI = PlotAPI()

    fun getPlot(location: Location): Plot? {
        return BukkitUtil.adapt(location).plot
    }

    fun hasPerm(player: Player, location: Location): Boolean {
        val location1 = BukkitUtil.adapt(location)
        if (!isPlotWorld(player.world)) {
            return true
        }
        val plot = location1.plot
        return hasPlotPerm(player.uniqueId, plot)
    }

    fun isPlotWorld(world: World): Boolean {
        return !plotAPI.getPlotAreas(world.name).isEmpty()
    }

    fun hasPlotPerm(uuid: UUID, plot: Plot?): Boolean {
        return plot != null && plot.isAdded(uuid)
    }
}
