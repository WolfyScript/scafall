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
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import kotlin.reflect.KClass

interface DataKeyProvider {

    val damage: ItemStackDataComponentConverter<Int>
    val repairCost: ItemStackDataComponentConverter<Int>
    val unbreakable: ItemStackDataComponentConverter<Unbreakable>
    val enchantments: ItemStackDataComponentConverter<Enchantments>
    val storedEnchantments: ItemStackDataComponentConverter<Enchantments>
    val customName: ItemStackDataComponentConverter<Component>
    val itemLore: ItemStackDataComponentConverter<ItemLore>
    val canBreak: ItemStackDataComponentConverter<CanBreak>
    val canPlaceOn: ItemStackDataComponentConverter<CanPlaceOn>
    val dyedColor: ItemStackDataComponentConverter<DyedColor>
    val attributeModifiers: ItemStackDataComponentConverter<AttributeModifiers>
    val chargedProjectiles: ItemStackDataComponentConverter<ChargedProjectiles>
    val intangibleProjectiles: ItemStackDataComponentConverter<IntangibleProjectiles>
    // TODO: map color
    // TODO: map decoration
    val mapId: ItemStackDataComponentConverter<Int>
    // TODO: map info
    val customModelData: ItemStackDataComponentConverter<Int>
    val potionEffects: ItemStackDataComponentConverter<PotionContents>
    // TODO: Writable Book Contents
    // TODO: Written Book Contents
    // TODO: Trim
    // TODO: Suspicious Stew
    // TODO: Hide Additional Tooltip
    // TODO: Debug Stick State
    // TODO: Entity Data
    // TODO: Bucket Entity Data
    val instrument: ItemStackDataComponentConverter<Key>
    val recipes: ItemStackDataComponentConverter<List<Key>>
    // TODO: Lodestone tracker
    val fireworkExplosion: ItemStackDataComponentConverter<FireworkExplosion>
    val fireworks: ItemStackDataComponentConverter<Fireworks>
    val profile: ItemStackDataComponentConverter<Profile>
    val noteBlockSound: ItemStackDataComponentConverter<Key>
    val baseColor: ItemStackDataComponentConverter<DyeColor>
    val bannerPatterns: ItemStackDataComponentConverter<BannerPatterns>
    val potDecorations: ItemStackDataComponentConverter<List<Key>>
    val container: ItemStackDataComponentConverter<Container>
    val bees: ItemStackDataComponentConverter<Bees>
    val lock: ItemStackDataComponentConverter<Lock>
    val containerLoot: ItemStackDataComponentConverter<ContainerLoot>
    val blockEntityData: ItemStackDataComponentConverter<BlockEntityData>
    val blockState: ItemStackDataComponentConverter<BlockState>
    val enchantmentGlintOverride: ItemStackDataComponentConverter<Boolean>
    val bundleContents: ItemStackDataComponentConverter<BundleContents>

    fun <T : Any> getDataKey(type: KClass<T>, key: Key) : DataKey<T, ItemStackLike<*, *>>

}