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
import kotlin.reflect.KClass

class PaperItemStackDataComponentConverter(scafall: Scafall) {

    fun register() {
        register(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersPaperConverter)
        register(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternsPaperConverter)
        register(ItemStackDataKeys.BREAK_SOUND, breakSoundConverter)
        register(ItemStackDataKeys.BASE_COLOR, baseColorConverter)
        register(ItemStackDataKeys.UNBREAKABLE, unbreakableConverter)
        register(ItemStackDataKeys.DAMAGE, damageConverter)
        register(ItemStackDataKeys.MAP_ID, mapIdConverter)
        register(ItemStackDataKeys.MAX_STACK_SIZE, maxStackSizeConverter)
        register(ItemStackDataKeys.MAX_DAMAGE, maxDamageConverter)
        register(ItemStackDataKeys.REPAIR_COST, repairCostConverter)
        register(ItemStackDataKeys.INSTRUMENT, instrumentConverter)
        register(ItemStackDataKeys.RECIPES, recipesConverter)
        register(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundConverter)
        register(ItemStackDataKeys.POT_DECORATIONS, potDecorationsConverter)
        register(ItemStackDataKeys.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverrideConverter)
        register(ItemStackDataKeys.GLIDER, gliderConverter)
        register(ItemStackDataKeys.ITEM_MODEL, itemModelConverter)
        register(ItemStackDataKeys.ITEM_NAME, itemNameConverter)
        register(ItemStackDataKeys.CUSTOM_NAME, customNameConverter)
        register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataConverter)
        register(ItemStackDataKeys.ITEM_LORE, itemLoreConverter)
        register(ItemStackDataKeys.CONTAINER_LOOT, containerLootConverter)
        register(ItemStackDataKeys.CONTAINER, containerConverter)
        register(ItemStackDataKeys.BUNDLE_CONTENTS, bundleContentsConverter)
        register(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesConverter)
        register(ItemStackDataKeys.DAMAGE_RESISTANT, damageResistantConverter)
        register(ItemStackDataKeys.RARITY, rarityConverter)
        register(ItemStackDataKeys.DEATH_PROTECTION, deathProtectionConverter)
        register(ItemStackDataKeys.OMINOUS_BOTTLE_AMPLIFIER, ominousBottleAmplifierConverter)
        register(ItemStackDataKeys.SUSPICIOUS_STEW_EFFECTS, suspiciousStewEffectsConverter)
        register(ItemStackDataKeys.TOOLTIP_STYLE, tooltipStyleConverter)
        register(ItemStackDataKeys.INTANGIBLE_PROJECTILE, intangibleProjectileConverter)
        register(ItemStackDataKeys.CONSUMABLE, consumableConverter)
        register(ItemStackDataKeys.ENCHANTABLE, enchantableConverter)
        register(ItemStackDataKeys.ENCHANTMENTS, enchantmentsConverter)
        register(ItemStackDataKeys.STORED_ENCHANTMENTS, storedEnchantmentsConverter)
        register(ItemStackDataKeys.REPAIRABLE, repairableConverter)
        register(ItemStackDataKeys.MAP_COLOR, mapColorConverter)
        register(ItemStackDataKeys.FOOD, foodConverter)
        register(ItemStackDataKeys.TOOL, toolConverter)
        register(ItemStackDataKeys.TRIM, trimConverter)
        register(ItemStackDataKeys.PROVIDES_BANNER_PATTERNS, providesBannerPatternsConverter)
        register(ItemStackDataKeys.TOOLTIP_DISPLAY, tooltipDisplayConverter)
        register(ItemStackDataKeys.BLOCKS_ATTACKS, blocksAttacksConverter)
        register(ItemStackDataKeys.USE_COOLDOWN, useCooldownConverter)
        register(ItemStackDataKeys.USE_REMAINDER, useRemainderConverter)
        register(ItemStackDataKeys.WEAPON, weaponConverter)
    }

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