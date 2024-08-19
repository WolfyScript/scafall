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

import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.Contract
import java.util.*

object ItemUtils {

    @JvmField
    val AIR: ItemStack = ItemStack(Material.AIR)

    @JvmStatic
    fun isEquipable(material: Material): Boolean {
        return when (material.name) {
            "ELYTRA", "CARVED_PUMPKIN" -> true
            else -> material.name.endsWith("_CHESTPLATE") || material.name.endsWith("_LEGGINGS") || material.name.endsWith(
                "_HELMET"
            ) || material.name.endsWith("_BOOTS") || material.name.endsWith("_HEAD") || material.name.endsWith("SKULL")
        }
    }

    @JvmStatic
    fun isEquipable(material: Material, type: ArmorType): Boolean {
        return when (type) {
            ArmorType.HELMET -> material.name.endsWith("_HELMET") || material.name.endsWith("_HEAD") || material.name.endsWith(
                "SKULL"
            ) || material == Material.CARVED_PUMPKIN

            ArmorType.CHESTPLATE -> material == Material.ELYTRA || material.name.endsWith("_CHESTPLATE")
            ArmorType.LEGGINGS -> material.name.endsWith("_LEGGINGS")
            ArmorType.BOOTS -> material.name.endsWith("_BOOTS")
        }
    }

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

    @JvmStatic
    @Contract(pure = true, value = "null -> true")
    fun isAirOrNull(item: CustomItem?): Boolean {
        return item == null || isAirOrNull(item.itemStack)
    }

    @JvmStatic
    fun applyNameAndLore(itemStack: ItemStack, displayName: Component, lore: List<Component?>): ItemStack {
        val itemBuilder = ItemBuilder(itemStack)
        val itemMeta = itemBuilder.itemMeta
        if (itemMeta != null) {
            itemBuilder.setDisplayName(BukkitComponentSerializer.legacy().serialize(displayName))
            if (!lore.isEmpty()) {
                itemBuilder.setLore(lore.stream().map { line: Component? ->
                    BukkitComponentSerializer.legacy().serialize(
                        line!!
                    )
                }.toList())
            }
        }
        return itemBuilder.create()
    }

    @JvmStatic
    @Deprecated("")
    fun replaceNameAndLore(miniMessage: MiniMessage, itemStack: ItemStack, tagResolver: TagResolver): ItemStack {
        val itemMeta = itemStack.itemMeta
        if (itemMeta != null) {
            val name = convertLegacyTextWithTagResolversToComponent(miniMessage, itemMeta.displayName, tagResolver)
            val legacyLore = if (itemMeta.hasLore()) itemMeta.lore!!
                .stream().map { s: String -> convertLegacyTextWithTagResolversToComponent(miniMessage, s, tagResolver) }
                .toList() else LinkedList()
            return applyNameAndLore(itemStack, name, legacyLore)
        }
        return itemStack
    }

    @JvmStatic
    @Deprecated("")
    fun replaceNameAndLore(
        miniMessage: MiniMessage,
        itemStack: ItemStack,
        vararg tagResolvers: TagResolver
    ): ItemStack {
        return replaceNameAndLore(miniMessage, itemStack, TagResolver.resolver(*tagResolvers))
    }

    @JvmStatic
    private fun convertLegacyTextWithTagResolversToComponent(
        miniMessage: MiniMessage,
        value: String,
        tagResolver: TagResolver
    ): Component {
        val lore = miniMessage.deserialize(value.replace("§", "&"), tagResolver)
        val converted = BukkitComponentSerializer.legacy().serialize(lore)
        return BukkitComponentSerializer.legacy().deserialize(converted.replace("&", "§"))
    }
}
