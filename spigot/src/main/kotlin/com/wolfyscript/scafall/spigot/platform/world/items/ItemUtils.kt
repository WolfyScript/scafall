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

import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.Contract

object ItemUtils {

    @JvmField
    val AIR: ItemStack = ItemStack(Material.AIR)

    @JvmStatic
    fun isTool(material: Material): Boolean {
        return when (material.name) {
            "WOODEN_HOE", "WOODEN_AXE", "WOODEN_PICKAXE", "WOODEN_SHOVEL", "WOODEN_SWORD" //WOODEN
                , "STONE_HOE", "STONE_AXE", "STONE_PICKAXE", "STONE_SHOVEL", "STONE_SWORD" //STONE
                , "IRON_HOE", "IRON_AXE", "IRON_PICKAXE", "IRON_SHOVEL", "IRON_SWORD" //IRON
                , "GOLDEN_HOE", "GOLDEN_AXE", "GOLDEN_PICKAXE", "GOLDEN_SHOVEL", "GOLDEN_SWORD" //GOLDEN
                , "DIAMOND_HOE", "DIAMOND_AXE", "DIAMOND_PICKAXE", "DIAMOND_SHOVEL", "DIAMOND_SWORD" //DIAMOND
                , "NETHERITE_HOE", "NETHERITE_AXE", "NETHERITE_PICKAXE", "NETHERITE_SHOVEL", "NETHERITE_SWORD" -> true

            else -> false
        }
    }

    @JvmStatic
    fun isAllowedInGrindStone(material: Material): Boolean {
        val equipmentSlot = material.equipmentSlot
        if (equipmentSlot != EquipmentSlot.HAND && equipmentSlot != EquipmentSlot.OFF_HAND) {
            return true
        }
        if (isTool(material)) return true
        return when (material.name) {
            "BOW", "CROSSBOW", "TRIDENT", "SHIELD", "TURTLE_HELMET", "ELYTRA", "CARROT_ON_A_STICK", "WARPED_FUNGUS_ON_A_STICK", "FISHING_ROD", "SHEARS", "FLINT_AND_STEEL", "ENCHANTED_BOOK" -> true
            else -> false
        }
    }

    @JvmStatic
    @Contract(pure = true, value = "null -> true")
    fun isAirOrNull(item: ItemStack?): Boolean {
        return item == null || item.type == Material.AIR
    }

}
