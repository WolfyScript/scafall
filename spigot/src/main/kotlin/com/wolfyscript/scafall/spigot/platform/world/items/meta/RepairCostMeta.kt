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
package com.wolfyscript.scafall.spigot.platform.world.items.meta

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import com.wolfyscript.scafall.spigot.platform.world.items.ItemBuilder
import org.bukkit.inventory.meta.Repairable

class RepairCostMeta : Meta(KEY) {
    init {
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.HIGHER, MetaSettings.Option.LOWER)
    }

    override fun check(item: CustomItem, itemOther: ItemBuilder): Boolean {
        val metaOther = itemOther.itemMeta
        val meta = item.itemMeta
        if (meta is Repairable) {
            if (metaOther is Repairable) {
                return when (option) {
                    MetaSettings.Option.EXACT -> metaOther.repairCost == meta.repairCost
                    MetaSettings.Option.LOWER -> metaOther.repairCost < meta.repairCost
                    MetaSettings.Option.HIGHER -> metaOther.repairCost > meta.repairCost
                    else -> false
                }
            }
            return false
        }
        return metaOther !is Repairable
    }

    companion object {
        val KEY: Key = scaffoldingKey("repair_cost")
    }
}