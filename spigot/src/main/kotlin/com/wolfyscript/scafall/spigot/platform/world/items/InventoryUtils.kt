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
package com.wolfyscript.scafall.spigot.platform.world.items

import com.google.common.collect.Streams
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.min

object InventoryUtils {
    fun isEmpty(list: List<ItemStack?>?): Boolean {
        if (list == null) return false
        return list.stream().allMatch { ItemUtils.isAirOrNull(it) }
    }

    fun isCustomItemsListEmpty(list: List<CustomItem?>?): Boolean {
        if (list == null) return false
        return list.stream().allMatch { ItemUtils.isAirOrNull(it) }
    }

    fun getInventorySpace(p: Player, item: ItemStack): Int {
        return getInventorySpace(p.inventory, item)
    }

    fun getInventorySpace(inventory: Inventory, item: ItemStack): Int {
        return getInventorySpace(inventory.storageContents, item)
    }

    fun getInventorySpace(contents: Array<ItemStack?>, item: ItemStack): Int {
        var free = 0
        for (i in contents) {
            if (ItemUtils.isAirOrNull(i)) {
                free += item.maxStackSize
            } else if (i!!.isSimilar(item)) {
                free += item.maxStackSize - i.amount
            }
        }
        return free
    }

    fun hasInventorySpace(inventory: Inventory, itemStack: ItemStack, amount: Int): Boolean {
        return getInventorySpace(inventory, itemStack) >= itemStack.amount * amount
    }

    fun hasInventorySpace(inventory: Inventory, itemStack: ItemStack): Boolean {
        return hasInventorySpace(inventory.storageContents, itemStack)
    }

    fun hasInventorySpace(contents: Array<ItemStack?>, itemStack: ItemStack): Boolean {
        return getInventorySpace(contents, itemStack) >= itemStack.amount
    }

    fun hasInventorySpace(p: Player, item: ItemStack): Boolean {
        return getInventorySpace(p, item) >= item.amount
    }

    fun hasEmptySpaces(p: Player, count: Int): Boolean {
        return Streams.stream(p.inventory).filter { obj: ItemStack? -> Objects.isNull(obj) }.count() >= count
    }

    fun firstSimilar(inventory: Inventory, itemStack: ItemStack): Int {
        for (i in 0 until inventory.size) {
            val slotItem = inventory.getItem(i) ?: return i
            if (itemStack.isSimilar(slotItem)) {
                if (slotItem.amount + itemStack.amount <= slotItem.maxStackSize) {
                    return i
                }
            }
        }
        return -1
    }

    @JvmOverloads
    fun calculateClickedSlot(
        event: InventoryClickEvent,
        cursor: ItemStack? = event.cursor,
        currentItem: ItemStack? = event.currentItem
    ) {
        if (cursor == null) return
        if (event.click.isLeftClick) {
            if (!ItemUtils.isAirOrNull(currentItem)) {
                event.isCancelled = true
                if (currentItem!!.isSimilar(cursor)) {
                    val possibleAmount = currentItem.maxStackSize - currentItem.amount
                    currentItem.amount = (currentItem.amount + (min(
                        cursor.amount.toDouble(),
                        possibleAmount.toDouble()
                    ))).toInt()
                    cursor.amount = cursor.amount - possibleAmount
                    event.currentItem = currentItem
                    event.setCursor(cursor)
                } else {
                    event.setCursor(currentItem)
                    event.currentItem = cursor
                }
            } else if (event.action != InventoryAction.PICKUP_ALL) {
                event.isCancelled = true
                event.setCursor(ItemUtils.AIR)
                event.currentItem = cursor
            }
        } else if (event.click.isRightClick) {
            if (!ItemUtils.isAirOrNull(currentItem)) {
                if (currentItem!!.isSimilar(cursor)) {
                    if (currentItem.amount < currentItem.maxStackSize && cursor.amount > 0) {
                        event.isCancelled = true
                        currentItem.amount = currentItem.amount + 1
                        cursor.amount = cursor.amount - 1
                    }
                } else {
                    event.isCancelled = true
                    event.setCursor(currentItem)
                    event.currentItem = cursor
                }
            } else {
                event.isCancelled = true
                val itemStack = cursor.clone()
                cursor.amount = cursor.amount - 1
                itemStack.amount = 1
                event.currentItem = itemStack
            }
        }
        if (event.whoClicked is Player) {
            (event.whoClicked as Player).updateInventory()
        }
    }
}
