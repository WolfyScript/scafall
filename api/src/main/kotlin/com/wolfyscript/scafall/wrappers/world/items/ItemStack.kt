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

import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.identifier.Key

interface ItemStack : DataHolder<ItemStack> {
    /**
     * The id representing the item of this ItemStack.<br></br>
     * Usually e.g. <pre>minecraft:&lt;item_id&gt;</pre>
     *
     * @return The id of the item.
     */
    val item: Key

    /**
     * The stack amount of this ItemStack.
     *
     * @return The stack amount.
     */
    val amount: Int

    /**
     * Creates a snapshot of the whole ItemStack including the full NBT.<br></br>
     * **This can be quite resource heavy!**<br></br>
     * The snapshot can be simply written to json using the Json mapper of WolfyUtils.
     *
     * @return The snapshot ItemStack config of this ItemStack.
     */
    fun snapshot(): ItemStackConfig
}
