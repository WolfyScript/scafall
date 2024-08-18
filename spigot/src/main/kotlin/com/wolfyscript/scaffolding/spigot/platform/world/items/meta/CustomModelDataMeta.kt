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
package com.wolfyscript.scaffolding.spigot.platform.world.items.meta

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.spigot.platform.world.items.CustomItem
import com.wolfyscript.scaffolding.spigot.platform.world.items.ItemBuilder


class CustomModelDataMeta : Meta(KEY) {
    init {
        setAvailableOptions(MetaSettings.Option.EXACT, MetaSettings.Option.HIGHER, MetaSettings.Option.LOWER)
    }

    override fun check(item: CustomItem, itemOther: ItemBuilder): Boolean {
        val meta1 = itemOther.itemMeta
        val meta2 = item.itemMeta
        return when (option) {
            MetaSettings.Option.LOWER -> meta1.customModelData < meta2.customModelData
            MetaSettings.Option.HIGHER -> meta1.customModelData > meta2.customModelData
            else -> false
        }
    }

    companion object {
        val KEY: Key = scaffoldingKey("custom_model_data")
    }
}
