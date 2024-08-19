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
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem.Companion.getByItemStack
import com.wolfyscript.scafall.spigot.platform.world.items.ItemBuilder

class CustomItemTagMeta : Meta(KEY) {

    override var option : MetaSettings.Option = MetaSettings.Option.EXACT
        set(value) { field = MetaSettings.Option.EXACT }

    override fun check(item: CustomItem, itemOther: ItemBuilder): Boolean {
        val key = item.key()
        val keyOther = getByItemStack(itemOther.itemStack)
        return keyOther != null && key == keyOther.key()
    }

    companion object {
        val KEY: Key = scaffoldingKey("customitem_tag")
    }
}
