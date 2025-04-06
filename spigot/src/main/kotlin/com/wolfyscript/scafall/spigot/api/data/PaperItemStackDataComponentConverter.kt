package com.wolfyscript.scafall.spigot.api.data

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper.*
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper.baseColorConverter
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper.damageConverter
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper.mapIdConverter
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper.maxStackSizeConverter
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper.unbreakableConverter
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.data.BlocksAttacks
import com.wolfyscript.scafall.wrappers.world.items.data.Repairable
import com.wolfyscript.scafall.wrappers.world.items.data.TooltipDisplay
import com.wolfyscript.scafall.wrappers.world.items.data.UseCooldown
import com.wolfyscript.scafall.wrappers.world.items.data.UseRemainder
import com.wolfyscript.scafall.wrappers.world.items.data.Weapon
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import kotlin.reflect.KClass

class PaperItemStackDataComponentConverter(scafall: Scafall) : SpigotItemStackDataComponentConverterProvider(scafall) {
    override val attributeModifiers = register(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersPaperConverter)
    override val bannerPatterns = register(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternsPaperConverter)
    override val breakSound = register(ItemStackDataKeys.BREAK_SOUND, breakSoundConverter)
    override val baseColor = register(ItemStackDataKeys.BASE_COLOR, baseColorConverter)
    override val unbreakable = register(ItemStackDataKeys.UNBREAKABLE, unbreakableConverter)
    override val damage = register(ItemStackDataKeys.DAMAGE, damageConverter)
    override val mapId = register(ItemStackDataKeys.MAP_ID, mapIdConverter)
    override val maxStackSize = register(ItemStackDataKeys.MAX_STACK_SIZE, maxStackSizeConverter)
    override val maxDamage = register(ItemStackDataKeys.MAX_DAMAGE, maxDamageConverter)
    override val repairCost = register(ItemStackDataKeys.REPAIR_COST, repairCostConverter)
    override val instrument = register(ItemStackDataKeys.INSTRUMENT, instrumentConverter)
    override val recipes = register(ItemStackDataKeys.RECIPES, recipesConverter)
    override val noteBlockSound = register(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundConverter)
    override val potDecorations = register(ItemStackDataKeys.POT_DECORATIONS, potDecorationsConverter)
    override val enchantmentGlintOverride = register(ItemStackDataKeys.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverrideConverter)
    override val glider = register(ItemStackDataKeys.GLIDER, gliderConverter)
    override val itemModel = register(ItemStackDataKeys.ITEM_MODEL, itemModelConverter)
    override val itemName = register(ItemStackDataKeys.ITEM_NAME, itemNameConverter)
    override val customName = register(ItemStackDataKeys.CUSTOM_NAME, customNameConverter)
    override val customModelData = register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataConverter)
    override val lore = register(ItemStackDataKeys.ITEM_LORE, itemLoreConverter)
    override val containerLoot = register(ItemStackDataKeys.CONTAINER_LOOT, containerLootConverter)
    override val container = register(ItemStackDataKeys.CONTAINER, containerConverter)
    override val bundleContents = register(ItemStackDataKeys.BUNDLE_CONTENTS, bundleContentsConverter)
    override val chargedProjectiles = register(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesConverter)
    override val damageResistant = register(ItemStackDataKeys.DAMAGE_RESISTANT, damageResistantConverter)
    override val rarity = register(ItemStackDataKeys.RARITY, rarityConverter)
    override val deathProtection = register(ItemStackDataKeys.DEATH_PROTECTION, deathProtectionConverter)
    override val ominousBottleAmplifier = register(ItemStackDataKeys.OMINOUS_BOTTLE_AMPLIFIER, ominousBottleAmplifierConverter)
    override val suspiciousStewEffects = register(ItemStackDataKeys.SUSPICIOUS_STEW_EFFECTS, suspiciousStewEffectsConverter)
    override val tooltipStyle: ItemStackDataComponentConverter<Key> = register(ItemStackDataKeys.TOOLTIP_STYLE, tooltipStyleConverter)
    override val intangibleProjectile = register(ItemStackDataKeys.INTANGIBLE_PROJECTILE, intangibleProjectileConverter)
    override val consumables = register(ItemStackDataKeys.CONSUMABLE, consumableConverter)
    override val enchantable = register(ItemStackDataKeys.ENCHANTABLE, enchantableConverter)
    override val enchantments = register(ItemStackDataKeys.ENCHANTMENTS, enchantmentsConverter)
    override val storedEnchantments = register(ItemStackDataKeys.STORED_ENCHANTMENTS, storedEnchantmentsConverter)
    override val repairable = register<Repairable>(ItemStackDataKeys.REPAIRABLE, repairableConverter)
    override val mapColor = register(ItemStackDataKeys.MAP_COLOR, mapColorConverter)
    override val food = register(ItemStackDataKeys.FOOD, foodConverter)
    override val tool = register(ItemStackDataKeys.TOOL, toolConverter)
    override val trim = register(ItemStackDataKeys.TRIM, trimConverter)
    override val providesBannerPatterns = register(ItemStackDataKeys.PROVIDES_BANNER_PATTERNS, providesBannerPatternsConverter)
    override val tooltipDisplay = register(ItemStackDataKeys.TOOLTIP_DISPLAY, tooltipDisplayConverter)
    override val blocksAttacks = register(ItemStackDataKeys.BLOCKS_ATTACKS, blocksAttacksConverter)
    override val useCooldown = register(ItemStackDataKeys.USE_COOLDOWN, useCooldownConverter)
    override val useRemainder = register(ItemStackDataKeys.USE_REMAINDER, useRemainderConverter)
    override val weapon = register(ItemStackDataKeys.WEAPON, weaponConverter)

    private inline fun <reified T: Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: PaperDataAPIConverter<T>
    ) : ItemStackDataComponentConverter<T> {
        val converterImpl = PaperItemStackDataComponentConverterImpl(dataKey.key(), T::class, converter);
        ScafallProvider.get().registries.itemStackDataComponentConverterRegistry.register(dataKey.key(), converterImpl)
        return converterImpl
    }

    private inline fun <reified T : Any> registerSpigot(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: ItemMetaDataKeyConverter<T>
    ) : ItemStackDataComponentConverter<T> {
        val converterImpl = SpigotItemStackDataComponentConverterImpl(
            dataKey.key(),
            T::class,
            converter.fetcher,
            converter.applier
        )
        ScafallProvider.get().registries.itemStackDataComponentConverterRegistry.register(dataKey.key(), converterImpl)
        return converterImpl
    }
}

class PaperItemStackDataComponentConverterImpl<T : Any>(
    override val key: Key,
    override val type: KClass<T>,
    converter: PaperDataAPIConverter<T>,
) : ItemStackDataComponentConverter<T> {

    override val reader: DataComponentConverter.Reader<T, ItemStackLike<*, *>> = object : DataComponentConverter.Reader<T, ItemStackLike<*,*>> {
        override val converter: ItemStackLike<*, *>.() -> Result<T?> = converter.reader
    }
    override val modifier: DataComponentConverter.Modifier<T, ItemStack> = object : DataComponentConverter.Modifier<T, ItemStack> {
        override val converter: ItemStack.(T) -> Result<ItemStack> = converter.modifierConverter
        override val remover: ItemStack.() -> Result<Pair<ItemStack, Boolean>> = converter.modifierRemover
    }
}

class PaperDataAPIConverter<T: Any>(
    val reader: ItemStackLike<*,*>.() -> Result<T?>,
    val modifierConverter: ItemStack.(T) -> Result<ItemStack>,
    val modifierRemover: ItemStack.() -> Result<Pair<ItemStack, Boolean>>,
)