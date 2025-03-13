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
import com.wolfyscript.scafall.wrappers.world.Color
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.data.Repairable
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component

interface DataComponentConverterProvider {

    val attributeModifiers: ItemStackDataComponentConverter<AttributeModifiers>
    val bannerPatterns: ItemStackDataComponentConverter<BannerPatterns>
    val baseColor: ItemStackDataComponentConverter<DyeColor>
    val bees: ItemStackDataComponentConverter<Bees>
    val blockEntityData: ItemStackDataComponentConverter<BlockEntityData>
    val blockState: ItemStackDataComponentConverter<BlockState>
    val bucketEntityData: ItemStackDataComponentConverter<BucketEntityData>
    val bundleContents: ItemStackDataComponentConverter<BundleContents>
    val canBreak: ItemStackDataComponentConverter<CanBreak>
    val canPlaceOn: ItemStackDataComponentConverter<CanPlaceOn>
    val chargedProjectiles: ItemStackDataComponentConverter<ChargedProjectiles>
    val consumables: ItemStackDataComponentConverter<Consumable>
    val container: ItemStackDataComponentConverter<Container>
    val containerLoot: ItemStackDataComponentConverter<ContainerLoot>
    val customData: ItemStackDataComponentConverter<CustomData>
    val customModelData: ItemStackDataComponentConverter<CustomModelData>
    val customName: ItemStackDataComponentConverter<Component>
    val damage: ItemStackDataComponentConverter<Int>
    val damageResistant: ItemStackDataComponentConverter<DamageResistant>
    val debugStickState: ItemStackDataComponentConverter<DebugStickState>
    val deathProtection: ItemStackDataComponentConverter<DeathProtection>
    val dyedColor: ItemStackDataComponentConverter<DyedColor>
    val enchantable: ItemStackDataComponentConverter<Enchantable>
    val enchantmentGlintOverride: ItemStackDataComponentConverter<Boolean>
    val enchantments: ItemStackDataComponentConverter<Enchantments>
    val entityData: ItemStackDataComponentConverter<EntityData>
    val equippable: ItemStackDataComponentConverter<Equippable>
    val fireworkExplosion: ItemStackDataComponentConverter<FireworkExplosion>
    val fireworks: ItemStackDataComponentConverter<Fireworks>
    val food: ItemStackDataComponentConverter<Food>
    val glider: ItemStackDataComponentConverter<Glider>
    val hideAdditionalTooltip: ItemStackDataComponentConverter<HideAdditionalTooltip>
    val hideTooltip: ItemStackDataComponentConverter<HideTooltip>
    val instrument: ItemStackDataComponentConverter<Key>
    val intangibleProjectile: ItemStackDataComponentConverter<IntangibleProjectile>
    val itemModel: ItemStackDataComponentConverter<Key>
    val itemName: ItemStackDataComponentConverter<Component>
    val jukeboxPlayable: ItemStackDataComponentConverter<JukeboxPlayable>
    val lodestoneTracker: ItemStackDataComponentConverter<LodestoneTracker>
    val lore: ItemStackDataComponentConverter<ItemLore>
    val lock: ItemStackDataComponentConverter<Lock>
    val mapColor: ItemStackDataComponentConverter<Color>
    val mapDecorations: ItemStackDataComponentConverter<MapDecorations>
    val mapId: ItemStackDataComponentConverter<Int>
    val maxDamage: ItemStackDataComponentConverter<Int>
    val maxStackSize: ItemStackDataComponentConverter<Int>
    val noteBlockSound: ItemStackDataComponentConverter<Key>
    val ominousBottleAmplifier: ItemStackDataComponentConverter<OminousBottleAmplifier>
    val potDecorations: ItemStackDataComponentConverter<List<Key>>
    val potionContents: ItemStackDataComponentConverter<PotionContents>
    val profile: ItemStackDataComponentConverter<Profile>
    val rarity: ItemStackDataComponentConverter<Rarity>
    val recipes: ItemStackDataComponentConverter<List<Key>>
    val repairable: ItemStackDataComponentConverter<Repairable>
    val repairCost: ItemStackDataComponentConverter<Int>
    val storedEnchantments: ItemStackDataComponentConverter<Enchantments>
    val suspiciousStewEffects: ItemStackDataComponentConverter<SuspiciousStewEffects>
    val tool: ItemStackDataComponentConverter<Tool>
    val tooltipStyle: ItemStackDataComponentConverter<Key>
    val trim: ItemStackDataComponentConverter<Trim>
    val unbreakable: ItemStackDataComponentConverter<Unbreakable>
    val useCooldown: ItemStackDataComponentConverter<UseCooldown>
    val useRemainder: ItemStackDataComponentConverter<UseRemainder>
    val writableBookContent: ItemStackDataComponentConverter<WriteableBookContent>
    val writtenBookContent: ItemStackDataComponentConverter<WrittenBookContent>

}