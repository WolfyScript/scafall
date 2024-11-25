package com.wolfyscript.scafall.spigot.api.data

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.MapColor
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.*
import kotlin.reflect.KClass

class SpigotItemStackDataComponentConverterProvider(private val scafall: Scafall) : DataComponentConverterProvider {

    override val damage = register(ItemStackDataKeys.DAMAGE, damageItemMetaConverter)
    override val damageResistant: ItemStackDataComponentConverter<DamageResistant>
        get() = TODO("Not yet implemented")
    override val debugStickState: ItemStackDataComponentConverter<DebugStickState>
        get() = TODO("Not yet implemented")
    override val deathProtection: ItemStackDataComponentConverter<DeathProtection>
        get() = TODO("Not yet implemented")
    override val repairCost = register<Int>(ItemStackDataKeys.REPAIR_COST, repairCostItemMetaConverter)
    override val unbreakable = register<Unbreakable>(ItemStackDataKeys.UNBREAKABLE, unbreakableItemMetaConverter)
    override val useCooldown: ItemStackDataComponentConverter<UseCooldown>
        get() = TODO("Not yet implemented")
    override val useRemainder: ItemStackDataComponentConverter<UseRemainder>
        get() = TODO("Not yet implemented")
    override val writableBookContent: ItemStackDataComponentConverter<WriteableBookContent>
        get() = TODO("Not yet implemented")
    override val writtenBookContent: ItemStackDataComponentConverter<WrittenBookContent>
        get() = TODO("Not yet implemented")
    override val enchantments = register<Enchantments>(ItemStackDataKeys.ENCHANTMENTS, enchantmentsItemMetaConverter)
    override val entityData: ItemStackDataComponentConverter<EntityData>
        get() = TODO("Not yet implemented")
    override val storedEnchantments = register<Enchantments>(ItemStackDataKeys.STORED_ENCHANTMENTS, enchantmentsItemMetaConverter)
    override val suspiciousStewEffects: ItemStackDataComponentConverter<SuspiciousStewEffects>
        get() = TODO("Not yet implemented")
    override val tool: ItemStackDataComponentConverter<Tool> = register(ItemStackDataKeys.TOOL, toolItemMetaConverter)
    override val tooltipStyle: ItemStackDataComponentConverter<Key> = register(ItemStackDataKeys.TOOLTIP_STYLE, tooltipStyleItemMetaConverter)
    override val trim: ItemStackDataComponentConverter<Trim>
        get() = TODO("Not yet implemented")
    override val customName = register<Component>(ItemStackDataKeys.CUSTOM_NAME, displayNameItemMetaConverter)
    override val lore = register<ItemLore>(ItemStackDataKeys.ITEM_LORE, itemLoreItemMetaConverter)
    override val canBreak = register<CanBreak>(ItemStackDataKeys.CAN_BREAK, canBreakItemMetaConverter)
    override val canPlaceOn = register<CanPlaceOn>(ItemStackDataKeys.CAN_PLACE_ON, canPlaceOnItemMetaConverter)
    override val dyedColor = register<DyedColor>(ItemStackDataKeys.DYED_COLOR, dyedColorItemMetaConverter)
    override val enchantable: ItemStackDataComponentConverter<Enchantable> = register<Enchantable>(ItemStackDataKeys.ENCHANTABLE, enchantableItemMetaConverter)
    override val attributeModifiers = register<AttributeModifiers>(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersItemMetaConverter)
    override val chargedProjectiles = register<ChargedProjectiles>(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesItemMetaConverter)
    override val consumables: ItemStackDataComponentConverter<Consumable>
        get() = TODO("Not yet implemented")
    override val intangibleProjectiles = register<IntangibleProjectiles>(ItemStackDataKeys.INTANGIBLE_PROJECTILES, intangibleProjectilesItemMetaConverter)
    override val itemModel: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
    override val itemName: ItemStackDataComponentConverter<Component>
        get() = TODO("Not yet implemented")
    override val jukeboxPlayable: ItemStackDataComponentConverter<JukeboxPlayable>
        get() = TODO("Not yet implemented")
    override val lodestoneTracker: ItemStackDataComponentConverter<LodestoneTracker>
        get() = TODO("Not yet implemented")
    override val mapId = register<Int>(ItemStackDataKeys.MAP_ID, mapIdItemMetaConverter)
    override val maxDamage: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val maxStackSize: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val customModelData = register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataItemMetaConverter)
    override val potionContents = register<PotionContents>(ItemStackDataKeys.POTION_CONTENTS, potionContentsItemMetaConverter)
    override val instrument = register<Key>(ItemStackDataKeys.INSTRUMENT, instrumentItemMetaConverter)
    override val recipes = register<List<Key>>(ItemStackDataKeys.RECIPES, recipesItemMetaConverter)
    override val repairable: ItemStackDataComponentConverter<com.wolfyscript.scafall.wrappers.world.items.data.Repairable>
        get() = TODO("Not yet implemented")
    override val fireworkExplosion = register(ItemStackDataKeys.FIREWORK_EXPLOSION, fireworkExplosionItemMetaConverter)
    override val fireworks = register(ItemStackDataKeys.FIREWORKS, fireworksItemMetaConverter)
    override val food: ItemStackDataComponentConverter<Food> = register<Food>(ItemStackDataKeys.FOOD, foodItemMetaConverter)
    override val glider: ItemStackDataComponentConverter<Glider>
        get() = TODO("Not yet implemented")
    override val hideAdditionalTooltip: ItemStackDataComponentConverter<HideAdditionalTooltip>
        get() = TODO("Not yet implemented")
    override val hideTooltip: ItemStackDataComponentConverter<HideTooltip>
        get() = TODO("Not yet implemented")
    override val profile = register<Profile>(ItemStackDataKeys.PROFILE, profileItemMetaConverter)
    override val rarity: ItemStackDataComponentConverter<Rarity>
        get() = TODO("Not yet implemented")
    override val noteBlockSound = register<Key>(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundItemMetaConverter)
    override val ominousBottleAmplifier: ItemStackDataComponentConverter<OminousBottleAmplifier>
        get() = TODO("Not yet implemented")
    override val baseColor = register<DyeColor>(ItemStackDataKeys.BASE_COLOR, baseColorItemMetaConverter)
    override val bannerPatterns = register<BannerPatterns>(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternsItemMetaConverter)
    override val potDecorations = register<List<Key>>(ItemStackDataKeys.POT_DECORATIONS, potDecorationsItemMetaConverter)
    override val container = register<Container>(ItemStackDataKeys.CONTAINER, containerItemMetaConverter)
    override val bees = register<Bees>(ItemStackDataKeys.BEES, beesItemMetaConverter)
    override val lock = register<Lock>(ItemStackDataKeys.LOCK, lockItemMetaConverter)
    override val mapColor: ItemStackDataComponentConverter<MapColor>
        get() = TODO("Not yet implemented")
    override val mapDecorations: ItemStackDataComponentConverter<MapDecorations>
        get() = TODO("Not yet implemented")
    override val containerLoot = register<ContainerLoot>(ItemStackDataKeys.CONTAINER_LOOT, containerLootItemMetaConverter)
    override val customData: ItemStackDataComponentConverter<CustomData>
        get() = TODO("Not yet implemented")
    override val blockEntityData = register<BlockEntityData>(ItemStackDataKeys.BLOCK_ENTITY_DATA, blockEntityItemMetaConverter)
    override val blockState = register<BlockState>(ItemStackDataKeys.BLOCK_STATE, blockStateItemMetaConverter)
    override val bucketEntityData: ItemStackDataComponentConverter<BucketEntityData>
        get() = TODO("Not yet implemented")
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
