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

package com.wolfyscript.scafall.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import kotlin.reflect.KClass

interface DataKeyProvider {

    val damage: DataKey<Int, ItemStack>
    val repairCost: DataKey<Int, ItemStack>
    val unbreakable: DataKey<Unbreakable, ItemStack>
    val enchantments: DataKey<Enchantments, ItemStack>
    val storedEnchantments: DataKey<Enchantments, ItemStack>
    val customName: DataKey<Component, ItemStack>
    val itemLore: DataKey<ItemLore, ItemStack>
    val canBreak: DataKey<CanBreak, ItemStack>
    val canPlaceOn: DataKey<CanPlaceOn, ItemStack>
    val dyedColor: DataKey<DyedColor, ItemStack>
    val attributeModifiers: DataKey<AttributeModifiers, ItemStack>
    val chargedProjectiles: DataKey<ChargedProjectiles, ItemStack>
    val intangibleProjectiles: DataKey<IntangibleProjectiles, ItemStack>
    // TODO: map color
    // TODO: map decoration
    val mapId: DataKey<Int, ItemStack>
    // TODO: map info
    val customModelData: DataKey<Int, ItemStack>
    val potionEffects: DataKey<PotionContents, ItemStack>
    // TODO: Writable Book Contents
    // TODO: Written Book Contents
    // TODO: Trim
    // TODO: Suspicious Stew
    // TODO: Hide Additional Tooltip
    // TODO: Debug Stick State
    // TODO: Entity Data
    // TODO: Bucket Entity Data
    val instrument: DataKey<Key, ItemStack>
    val recipes: DataKey<List<Key>, ItemStack>
    // TODO: Lodestone tracker
    val fireworkExplosion: DataKey<FireworkExplosion, ItemStack>
    val fireworks: DataKey<Fireworks, ItemStack>
    val profile: DataKey<Profile, ItemStack>
    val noteBlockSound: DataKey<Key, ItemStack>
    val baseColor: DataKey<DyeColor, ItemStack>
    val bannerPatterns: DataKey<BannerPatterns, ItemStack>
    val potDecorations: DataKey<List<Key>, ItemStack>
    val container: DataKey<Container, ItemStack>
    val bees: DataKey<Bees, ItemStack>
    val lock: DataKey<String, ItemStack>
    val containerLoot: DataKey<ContainerLoot, ItemStack>
    val blockEntityData: DataKey<BlockEntityData, ItemStack>
    val blockState: DataKey<BlockState, ItemStack>
    val enchantmentGlintOverride: DataKey<Boolean, ItemStack>
    val bundleContents: DataKey<BundleContents, ItemStack>


    fun <T : Any> getDataKey(type: KClass<T>, key: Key) : DataKey<T, ItemStack>

}