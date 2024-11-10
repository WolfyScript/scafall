package com.wolfyscript.scafall.spigot.api.data

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.function.ReceiverBiConsumer
import com.wolfyscript.scafall.function.ReceiverFunction
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStack
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.*
import kotlin.reflect.KClass

class SpigotItemStackDataKeyProvider(private val scafall: Scafall) : DataKeyProvider {

    override val damage = register(ItemStackDataKeys.DAMAGE, damageItemMetaConverter)
    override val repairCost = register<Int>(ItemStackDataKeys.REPAIR_COST, repairCostItemMetaConverter)
    override val unbreakable = register<Unbreakable>(ItemStackDataKeys.UNBREAKABLE, unbreakableItemMetaConverter)
    override val enchantments = register<Enchantments>(ItemStackDataKeys.ENCHANTMENTS, enchantmentsItemMetaConverter)
    override val storedEnchantments = register<Enchantments>(ItemStackDataKeys.STORED_ENCHANTMENTS, enchantmentsItemMetaConverter)
    override val customName = register<Component>(ItemStackDataKeys.CUSTOM_NAME, displayNameItemMetaConverter)
    override val itemLore = register<ItemLore>(ItemStackDataKeys.ITEM_LORE, itemLoreItemMetaConverter)
    override val canBreak = register<CanBreak>(ItemStackDataKeys.CAN_BREAK, canBreakItemMetaConverter)
    override val canPlaceOn = register<CanPlaceOn>(ItemStackDataKeys.CAN_PLACE_ON, canPlaceOnItemMetaConverter)
    override val dyedColor = register<DyedColor>(ItemStackDataKeys.DYED_COLOR, dyedColorItemMetaConverter)
    override val attributeModifiers = register<AttributeModifiers>(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersItemMetaConverter)
    override val chargedProjectiles = register<ChargedProjectiles>(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesItemMetaConverter)
    override val intangibleProjectiles = register<IntangibleProjectiles>(ItemStackDataKeys.INTANGIBLE_PROJECTILES, intangibleProjectilesItemMetaConverter)
    // TODO: Map Color
    // TODO: map Decorations
    override val mapId = register<Int>(ItemStackDataKeys.MAP_ID, mapIdItemMetaConverter)
    // TODO: Map Info
    override val customModelData = register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataItemMetaConverter)
    override val potionEffects = register<PotionContents>(ItemStackDataKeys.POTION_CONTENTS, potionContentsItemMetaConverter)
    // TODO: Writable Book Contents
    // TODO: Written Book Contents
    // TODO: Trim
    // TODO: Suspicious Stew
    // TODO: Hide Additional Tooltip
    // TODO: Debug Stick State
    // TODO: Entity Data
    // TODO: Bucket Entity Data
    override val instrument = register<Key>(ItemStackDataKeys.INSTRUMENT, instrumentItemMetaConverter)
    override val recipes = register<List<Key>>(ItemStackDataKeys.RECIPES, recipesItemMetaConverter)
    // TODO: Lodestone Tracker
    override val fireworkExplosion = register(ItemStackDataKeys.FIREWORKS_EXPLOSION, fireworkExplosionItemMetaConverter)
    override val fireworks = register(ItemStackDataKeys.FIREWORKS, fireworksItemMetaConverter)
    override val profile = register<Profile>(ItemStackDataKeys.PROFILE, profileItemMetaConverter)
    override val noteBlockSound = register<Key>(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundItemMetaConverter)
    override val baseColor = register<DyeColor>(ItemStackDataKeys.BASE_COLOR, baseColorItemMetaConverter)
    override val bannerPatterns = register<BannerPatterns>(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternsItemMetaConverter)
    override val potDecorations = register<List<Key>>(ItemStackDataKeys.POT_DECORATIONS, potDecorationsItemMetaConverter)
    override val container = register<Container>(ItemStackDataKeys.CONTAINER, containerItemMetaConverter)
    override val bees = register<Bees>(ItemStackDataKeys.BEES, beesItemMetaConverter)
    override val lock = register<Lock>(ItemStackDataKeys.LOCK, lockItemMetaConverter)
    override val containerLoot = register<ContainerLoot>(ItemStackDataKeys.CONTAINER_LOOT, containerLootItemMetaConverter)
    override val blockEntityData = register<BlockEntityData>(ItemStackDataKeys.BLOCK_ENTITY_DATA, blockEntityItemMetaConverter)
    override val blockState = register<BlockState>(ItemStackDataKeys.BLOCK_STATE, blockStateItemMetaConverter)
    override val enchantmentGlintOverride = register<Boolean>(ItemStackDataKeys.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverrideItemMetaConverter)
    override val bundleContents = register<BundleContents>(ItemStackDataKeys.BUNDLE_CONTENTS, bundleContentsItemMetaConverter)

    private inline fun <reified T : Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: ItemMetaDataKeyConverter<T>
    ) : ItemStackDataComponentConverter<T> {
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

data class ItemMetaDataKeyConverter<T: Any>(val fetcher: ItemMeta.() -> T?, val applier: ItemMeta.(T) -> Unit)

class ItemStackDataComponentConverterImpl<T : Any, H : DataHolder<H, *>>(
    override val key: Key,
    override val type: KClass<T>,
    reader: ItemMeta.() -> T?,
    writer: ItemMeta.(T) -> Unit
) : ItemStackDataComponentConverter<T> {

    override val reader: DataComponentConverter.Reader<T, ItemStackLike<*, *>> =
        object : DataComponentConverter.Reader<T, ItemStackLike<*, *>> {
            override val converter: ItemStackLike<*, *>.() -> T? = {
                unwrap().itemMeta?.reader()
            }
        }

    override val writer: DataComponentConverter.Writer<T, ItemStack> =
        object : DataComponentConverter.Writer<T, ItemStack> {
            override val converter: ItemStack.(T) -> ItemStack = {
                val ref = unwrap()
                ref.itemMeta?.let { meta ->
                    meta.writer(it)
                    ref.itemMeta = meta
                }
                this
            }
        }

}
