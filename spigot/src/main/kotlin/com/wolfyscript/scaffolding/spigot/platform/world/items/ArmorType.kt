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

import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

enum class ArmorType(val slot: Int, val equipmentSlot: EquipmentSlot) {
    HELMET(39, EquipmentSlot.HEAD),
    CHESTPLATE(38, EquipmentSlot.CHEST),
    LEGGINGS(37, EquipmentSlot.LEGS),
    BOOTS(36, EquipmentSlot.FEET);

    companion object {
        fun getBySlot(slot: Int): ArmorType? {
            for (armorType in entries) {
                if (armorType.slot == slot) return armorType
            }
            return null
        }

        fun getBySlot(slot: EquipmentSlot): ArmorType? {
            for (armorType in entries) {
                if (armorType.equipmentSlot == slot) return armorType
            }
            return null
        }

        fun matchType(customItem: CustomItem?): ArmorType? {
            return matchType(null, customItem, null)
        }

        fun matchType(customItem: CustomItem?, playerInventory: PlayerInventory?): ArmorType? {
            return matchType(null, customItem, playerInventory)
        }

        fun matchType(itemStack: ItemStack?, playerInventory: PlayerInventory?): ArmorType? {
            return matchType(itemStack, null, playerInventory)
        }

        @JvmOverloads
        fun matchType(
            itemStack: ItemStack?,
            customItem: CustomItem? = null,
            playerInventory: PlayerInventory? = null
        ): ArmorType? {
            if (!ItemUtils.isAirOrNull(customItem) && customItem!!.hasEquipmentSlot()) {
                if (playerInventory == null) return getBySlot(customItem.getEquipmentSlots()[0])
                val armorType = customItem.getEquipmentSlots().stream().map { slot: EquipmentSlot -> getBySlot(slot) }
                    .filter { type: ArmorType? ->
                        ItemUtils.isAirOrNull(
                            playerInventory.getItem(
                                type!!.slot
                            )
                        )
                    }.findFirst().orElse(null)
                if (armorType != null) {
                    return armorType
                } else if (customItem.isBlockVanillaEquip) {
                    return null
                }
            }
            if (itemStack != null) {
                val type = itemStack.type.name
                var armorType: ArmorType? = null
                if (type.endsWith("_HELMET") || type.endsWith("_SKULL")) armorType = HELMET
                else if (type.endsWith("_CHESTPLATE") || type.endsWith("ELYTRA")) armorType = CHESTPLATE
                else if (type.endsWith("_LEGGINGS")) armorType = LEGGINGS
                else if (type.endsWith("_BOOTS")) armorType = BOOTS
                if (armorType != null) {
                    if (playerInventory != null && !ItemUtils.isAirOrNull(playerInventory.getItem(armorType.slot))) return null
                    return armorType
                }
            }
            return null
        }
    }
}
