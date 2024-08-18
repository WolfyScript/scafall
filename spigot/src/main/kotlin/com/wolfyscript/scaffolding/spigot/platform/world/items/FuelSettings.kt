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
package com.wolfyscript.scaffolding.spigot.platform.world.items

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.wolfyscript.scaffolding.Copyable
import org.bukkit.Material
import java.util.*

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class FuelSettings : Copyable<FuelSettings> {
    @JsonAlias("allowed_blocks")
    var allowedBlocks: List<Material> = ArrayList()

    @JsonAlias("burn_time", "burntime")
    var burnTime: Int = 20

    constructor()

    constructor(settings: FuelSettings) {
        this.allowedBlocks = ArrayList(settings.allowedBlocks)
        this.burnTime = settings.burnTime
    }

    override fun copy(): FuelSettings {
        return FuelSettings(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as FuelSettings
        return burnTime == that.burnTime && allowedBlocks == that.allowedBlocks
    }

    override fun hashCode(): Int {
        return Objects.hash(allowedBlocks, burnTime)
    }
}
