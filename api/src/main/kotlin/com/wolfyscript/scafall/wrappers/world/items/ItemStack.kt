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
package com.wolfyscript.scafall.wrappers.world.items

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.identifier.Key

/**
 * A mutable version of the ItemStack.
 * i.e. The [DataComponentMap] and possibly other properties can be modified directly and the same instance will reflect those changes.
 *
 * @see ItemStackSnapshot An immutable snapshot of the ItemStack
 */
interface ItemStack : DataHolder.Mutable<ItemStack>, ItemStackLike<ItemStack, DataComponentMap.Mutable<ItemStack>> {

    companion object {

        fun of(itemType: Key): ItemStack {
            return ScafallProvider.get().factories.itemsFactory.createStack(itemType)
        }

        fun of(mcItemType: String) : ItemStack {
            return of(Key.key(Key.MINECRAFT_NAMESPACE, mcItemType))
        }

    }

    /**
     * Creates a snapshot of the whole ItemStack
     *
     * @return The snapshot ItemStack of this ItemStack.
     */
    fun snapshot(): ItemStackSnapshot

}
