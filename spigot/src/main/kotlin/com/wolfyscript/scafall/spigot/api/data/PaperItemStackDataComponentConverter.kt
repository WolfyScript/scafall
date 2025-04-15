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

class PaperItemStackDataComponentConverter(val scafall: Scafall) {

    fun register() {
        register(ItemDataComponentTypes.ATTRIBUTE_MODIFIERS, attributeModifiersPaperConverter)
        register(ItemDataComponentTypes.BANNER_PATTERNS, bannerPatternsPaperConverter)
        register(ItemDataComponentTypes.BREAK_SOUND, breakSoundConverter)
        register(ItemDataComponentTypes.BASE_COLOR, baseColorConverter)
        register(ItemDataComponentTypes.UNBREAKABLE, unbreakableConverter)
        register(ItemDataComponentTypes.DAMAGE, damageConverter)
        register(ItemDataComponentTypes.MAP_ID, mapIdConverter)
        register(ItemDataComponentTypes.MAX_STACK_SIZE, maxStackSizeConverter)
        register(ItemDataComponentTypes.MAX_DAMAGE, maxDamageConverter)
        register(ItemDataComponentTypes.REPAIR_COST, repairCostConverter)
        register(ItemDataComponentTypes.INSTRUMENT, instrumentConverter)
        register(ItemDataComponentTypes.RECIPES, recipesConverter)
        register(ItemDataComponentTypes.NOTE_BLOCK_SOUND, noteBlockSoundConverter)
        register(ItemDataComponentTypes.POT_DECORATIONS, potDecorationsConverter)
        register(ItemDataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverrideConverter)
        register(ItemDataComponentTypes.GLIDER, gliderConverter)
        register(ItemDataComponentTypes.ITEM_MODEL, itemModelConverter)
        register(ItemDataComponentTypes.ITEM_NAME, itemNameConverter)
        register(ItemDataComponentTypes.CUSTOM_NAME, customNameConverter)
        register(ItemDataComponentTypes.CUSTOM_MODEL_DATA, customModelDataConverter)
        register(ItemDataComponentTypes.ITEM_LORE, itemLoreConverter)
        register(ItemDataComponentTypes.CONTAINER_LOOT, containerLootConverter)
        register(ItemDataComponentTypes.CONTAINER, containerConverter)
        register(ItemDataComponentTypes.BUNDLE_CONTENTS, bundleContentsConverter)
        register(ItemDataComponentTypes.CHARGED_PROJECTILES, chargedProjectilesConverter)
        register(ItemDataComponentTypes.DAMAGE_RESISTANT, damageResistantConverter)
        register(ItemDataComponentTypes.RARITY, rarityConverter)
        register(ItemDataComponentTypes.DEATH_PROTECTION, deathProtectionConverter)
        register(ItemDataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER, ominousBottleAmplifierConverter)
        register(ItemDataComponentTypes.SUSPICIOUS_STEW_EFFECTS, suspiciousStewEffectsConverter)
        register(ItemDataComponentTypes.TOOLTIP_STYLE, tooltipStyleConverter)
        register(ItemDataComponentTypes.INTANGIBLE_PROJECTILE, intangibleProjectileConverter)
        register(ItemDataComponentTypes.CONSUMABLE, consumableConverter)
        register(ItemDataComponentTypes.ENCHANTABLE, enchantableConverter)
        register(ItemDataComponentTypes.ENCHANTMENTS, enchantmentsConverter)
        register(ItemDataComponentTypes.STORED_ENCHANTMENTS, storedEnchantmentsConverter)
        register(ItemDataComponentTypes.REPAIRABLE, repairableConverter)
        register(ItemDataComponentTypes.MAP_COLOR, mapColorConverter)
        register(ItemDataComponentTypes.FOOD, foodConverter)
        register(ItemDataComponentTypes.TOOL, toolConverter)
        register(ItemDataComponentTypes.TRIM, trimConverter)
        register(ItemDataComponentTypes.PROVIDES_BANNER_PATTERNS, providesBannerPatternsConverter)
        register(ItemDataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplayConverter)
        register(ItemDataComponentTypes.BLOCKS_ATTACKS, blocksAttacksConverter)
        register(ItemDataComponentTypes.USE_COOLDOWN, useCooldownConverter)
        register(ItemDataComponentTypes.USE_REMAINDER, useRemainderConverter)
        register(ItemDataComponentTypes.WEAPON, weaponConverter)

        // Register missing converters with spigot implementations
        SpigotItemStackDataComponentConverterProvider(scafall).register()
    }

    private inline fun <reified T: Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: PaperDataAPIConverter<T>
    ) : ItemDataComponentConverter<T> {
        val converterImpl = PaperItemDataComponentConverterImpl(dataKey.key(), T::class, converter);
        ScafallProvider.get().registries.itemDataComponentConverterRegistry.register(dataKey.key(), converterImpl)
        return converterImpl
    }

    private inline fun <reified T : Any> registerSpigot(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: ItemMetaDataKeyConverter<T>
    ) : ItemDataComponentConverter<T> {
        val converterImpl = SpigotItemDataComponentConverterImpl(
            dataKey.key(),
            T::class,
            converter.fetcher,
            converter.applier
        )
        ScafallProvider.get().registries.itemDataComponentConverterRegistry.register(dataKey.key(), converterImpl)
        return converterImpl
    }
}

class PaperItemDataComponentConverterImpl<T : Any>(
    override val key: Key,
    override val type: KClass<T>,
    converter: PaperDataAPIConverter<T>,
) : ItemDataComponentConverter<T> {

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