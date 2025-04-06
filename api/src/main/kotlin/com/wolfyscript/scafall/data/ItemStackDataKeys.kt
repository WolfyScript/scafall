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

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.nbt.NBTTagConfigCompound
import com.wolfyscript.scafall.wrappers.world.Color
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.MapColor
import com.wolfyscript.scafall.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import net.kyori.adventure.text.Component
import kotlin.reflect.KClass

/**
 * A Collection of the default/vanilla [DataKeys][DataKey] that link to Data stored on ItemStacks.
 *
 * The data can be fetched, applied, and removed to/from ItemStacks via [ItemStack.data]
 */
interface ItemStackDataKeys {

    companion object {
        val ATTRIBUTE_MODIFIERS = register<AttributeModifiers>("attribute_modifiers")
        val BANNER_PATTERNS = register<BannerPatterns>("banner_patterns")
        val BASE_COLOR = register<DyeColor>("base_color")
        val BEES = register<Bees>("bees")
        val BLOCK_ENTITY_DATA = register<BlockEntityData>("block_entity_data")
        val BLOCK_STATE = register<BlockState>("block_state")
        val BLOCKS_ATTACKS = register<BlocksAttacks>("blocks_attacks")
        val BUCKET_ENTITY_DATA = register<BucketEntityData>("bucket_entity_data")
        val BUNDLE_CONTENTS = register<BundleContents>("bundle_contents")
        val BREAK_SOUND = register<SoundEvent>("break_sound")
        val CAN_BREAK = register<CanBreak>("can_break")
        val CAN_PLACE_ON = register<CanPlaceOn>("can_place_on")
        val CHARGED_PROJECTILES = register<ChargedProjectiles>("charged_projectiles")
        val CONSUMABLE = register<Consumable>("consumable")
        val CONTAINER = register<Container>("container")
        val CONTAINER_LOOT = register<ContainerLoot>("container_loot")
        val CUSTOM_DATA = register<NBTTagConfigCompound>("custom_data")
        val CUSTOM_MODEL_DATA = register<CustomModelData>("custom_model_data")
        val CUSTOM_NAME = register<Component>("custom_name")
        val DAMAGE = register<Int>("damage")
        val DAMAGE_RESISTANT = register<DamageResistant>("damage_resistant")
        val DEBUG_STICK_STATE = register<DebugStickState>("debug_stick_state")
        val DEATH_PROTECTION = register<DeathProtection>("death_protection")
        val DYED_COLOR = register<DyedColor>("dyed_color")
        val ENCHANTABLE = register<Enchantable>("enchantable")
        val ENCHANTMENTS = register<Enchantments>("enchantments")
        val ENCHANTMENT_GLINT_OVERRIDE = register<Boolean>("enchantment_glint_override")
        val ENTITY_DATA = register<EntityData>("entity_data")
        val EQUIPPABLE = register<Equippable>("equippable")
        val FIREWORKS = register<Fireworks>("fireworks")
        val FIREWORK_EXPLOSION = register<FireworkExplosion>("firework_explosion")
        val FOOD = register<Food>("food")
        val GLIDER = register<Glider>("glider")
        val INSTRUMENT = register<Key>("instrument")
        val INTANGIBLE_PROJECTILE = register<IntangibleProjectile>("intangible_projectile")
        val ITEM_MODEL = register<Key>("item_model")
        val ITEM_NAME = register<Component>("item_name")
        val JUKEBOX_PLAYABLE = register<JukeboxPlayable>("jukebox_playable")
        val ITEM_LORE = register<ItemLore>("item_lore")
        val LOCK = register<Lock>("lock")
        val LODESTONE_TRACKER = register<LodestoneTracker>("lodestone_tracker")
        val MAP_COLOR = register<Color>("map_color")
        val MAP_DECORATIONS = register<MapDecorations>("map_decorations")
        val MAP_ID = register<Int>("map_id")
        val MAX_DAMAGE = register<Int>("max_damage")
        val MAX_STACK_SIZE = register<Int>("max_stack_size")
        val NOTE_BLOCK_SOUND = register<Key>("note_block_sound")
        val OMINOUS_BOTTLE_AMPLIFIER = register<OminousBottleAmplifier>("ominous_bottle_amplifier")
        val POTION_CONTENTS = register<PotionContents>("potion_contents")
        val POT_DECORATIONS = register<List<Key>>("pot_decorations")
        val PROFILE = register<Profile>("profile")
        val PROVIDES_BANNER_PATTERNS = register<Key>("provides_banner_patterns")
        val PROVIDES_TRIM_MATERIAL = register<Key>("provides_trim_material")
        val RARITY = register<Rarity>("rarity")
        val RECIPES = register<List<Key>>("recipes")
        val REPAIRABLE = register<Repairable>("repairable")
        val REPAIR_COST = register<Int>("repair_cost")
        val STORED_ENCHANTMENTS = register<Enchantments>("stored_enchantments")
        val SUSPICIOUS_STEW_EFFECTS = register<SuspiciousStewEffects>("suspicious_stew_effects")
        val TOOL = register<Tool>("tool")
        val TOOLTIP_DISPLAY = register<TooltipDisplay>("tooltip_display")
        val TOOLTIP_STYLE = register<Key>("tooltip_style")
        val TRIM = register<Trim>("trim")
        val UNBREAKABLE = register<Unbreakable>("unbreakable")
        val USE_COOLDOWN = register<UseCooldown>("use_cooldown")
        val USE_REMAINDER = register<UseRemainder>("use_remainder")
        val WEAPON = register<Weapon>("weapon")
        val WRITABLE_BOOK_CONTENTS = register<WriteableBookContent>("writable_book_contents")
        val WRITTEN_BOOK_CONTENTS = register<WrittenBookContent>("written_book_contents")

        fun <T : Any> register(type: KClass<T>, key: Key): DataKey<T, ItemStackLike<*, *>> {
            val dataKey = ScafallProvider.get().factories.dataKeyFactory.create<T, ItemStackLike<*,*>>(type, key)
            ScafallProvider.get().registries.itemDataKeyRegistry.register(key, dataKey)
            return dataKey
        }

        fun <T : Any> register(type: KClass<T>, key: String): DataKey<T, ItemStackLike<*, *>> {
            return register(type, Key.key(Key.MINECRAFT_NAMESPACE, key))
        }

        inline fun <reified T : Any> register(key: String): DataKey<T, ItemStackLike<*, *>> {
            return register(T::class, key)
        }
    }

}
