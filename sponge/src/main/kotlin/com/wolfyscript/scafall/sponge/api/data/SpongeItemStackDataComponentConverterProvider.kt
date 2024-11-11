package com.wolfyscript.scafall.sponge.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.unwrap
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import kotlin.reflect.KClass

class SpongeItemStackDataComponentConverterProvider : DataComponentConverterProvider {

    override val damage = register(ItemStackDataKeys.DAMAGE, damageConverter)
    override val repairCost = register(ItemStackDataKeys.REPAIR_COST, repairCostConverter)
    override val unbreakable = register(ItemStackDataKeys.UNBREAKABLE, unbreakableDataConverter)
    override val enchantments = register(ItemStackDataKeys.ENCHANTMENTS, enchantmentsDataConverter)
    override val storedEnchantments = register(ItemStackDataKeys.STORED_ENCHANTMENTS, enchantmentsDataConverter)
    override val customName = register(ItemStackDataKeys.CUSTOM_NAME, displayNameConverter)
    override val itemLore = register(ItemStackDataKeys.ITEM_LORE, displayLoreConverter)
    override val canBreak = register(ItemStackDataKeys.CAN_BREAK, canBreakDataConverter)
    override val canPlaceOn = register(ItemStackDataKeys.CAN_PLACE_ON, canPlaceOnDataConverter)
    override val dyedColor = register(ItemStackDataKeys.DYED_COLOR, dyedColorDataConverter)
    override val attributeModifiers = register(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersDataConverter)
    override val chargedProjectiles = register(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesDataConverter)
    override val intangibleProjectiles = register(ItemStackDataKeys.INTANGIBLE_PROJECTILES, intangibleProjectilesDataConverter)
    override val mapId = register(ItemStackDataKeys.MAP_ID, mapIdDataConverter)
    override val customModelData = register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataConverter)
    override val potionEffects = register(ItemStackDataKeys.POTION_CONTENTS, potionContentsDataConverter)
    override val instrument = register(ItemStackDataKeys.INSTRUMENT, instrumentConverter)
    override val recipes = register(ItemStackDataKeys.RECIPES, recipesDataConverter)
    override val fireworkExplosion = register(ItemStackDataKeys.FIREWORKS_EXPLOSION, fireworkExplosionDataConverter)
    override val fireworks = register(ItemStackDataKeys.FIREWORKS, fireworksDataConverter)
    override val profile = register(ItemStackDataKeys.PROFILE, profileDataConverter)
    override val noteBlockSound = register(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundConverter)
    override val baseColor = register(ItemStackDataKeys.BASE_COLOR, baseColorDataConverter)
    override val bannerPatterns = register(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternDataConverter)
    override val potDecorations = register(ItemStackDataKeys.POT_DECORATIONS, potDecorationsDataConverter)
    override val container = register(ItemStackDataKeys.CONTAINER, containerDataConverter)
    override val bees = register(ItemStackDataKeys.BEES, beesDataConverter)
    override val lock = register(ItemStackDataKeys.LOCK, lockDataConverter)
    override val containerLoot = register(ItemStackDataKeys.CONTAINER_LOOT, containerLootDataConverter)
    override val blockEntityData = register(ItemStackDataKeys.BLOCK_ENTITY_DATA, blockEntityDataConverter)
    override val blockState = register(ItemStackDataKeys.BLOCK_STATE, blockStateDataConverter)
    override val enchantmentGlintOverride = register(ItemStackDataKeys.ENCHANTMENT_GLINT_OVERRIDE, enchantmentOverrideDataConverter)
    override val bundleContents = register(ItemStackDataKeys.BUNDLE_CONTENTS, bundleContentsDataConverter)

    private inline fun <reified T : Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: SpongeItemStackDataComponentConverter<T>
    ): ItemStackDataComponentConverter<T> {
        val converterImpl = ItemStackDataComponentConverterImpl(
            dataKey.key(),
            T::class,
            converter.fetcher,
            converter.applier
        )
        ScafallProvider.get().registries.itemStackDataComponentConverterRegistry.register(dataKey.key(), converterImpl)
        return converterImpl
    }

}

data class SpongeItemStackDataComponentConverter<T : Any>(
    val fetcher: org.spongepowered.api.item.inventory.ItemStackLike.() -> T?,
    val applier: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit
)

class ItemStackDataComponentConverterImpl<T : Any, H : DataHolder<H, *>>(
    override val key: Key,
    override val type: KClass<T>,
    reader: org.spongepowered.api.item.inventory.ItemStackLike.() -> T?,
    writer: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit
) : ItemStackDataComponentConverter<T> {

    override val reader: DataComponentConverter.Reader<T, ItemStackLike<*, *>> =
        object : DataComponentConverter.Reader<T, ItemStackLike<*, *>> {
            override val converter: ItemStackLike<*, *>.() -> T? = {
                unwrap().reader()
            }
        }

    override val writer: DataComponentConverter.Writer<T, ItemStack> =
        object : DataComponentConverter.Writer<T, ItemStack> {
            override val converter: ItemStack.(T) -> ItemStack = {
                unwrap().writer(it)
                this
            }
        }

}
