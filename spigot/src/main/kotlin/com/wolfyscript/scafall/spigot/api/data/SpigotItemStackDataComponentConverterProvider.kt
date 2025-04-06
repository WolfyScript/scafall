package com.wolfyscript.scafall.spigot.api.data

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.Color
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.*
import kotlin.reflect.KClass

open class SpigotItemStackDataComponentConverterProvider(private val scafall: Scafall) : DataComponentConverterProvider {

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
    override val weapon: ItemStackDataComponentConverter<Weapon>
        get() = TODO("Not yet implemented")
    override val writableBookContent: ItemStackDataComponentConverter<WriteableBookContent>
        get() = TODO("Not yet implemented")
    override val writtenBookContent: ItemStackDataComponentConverter<WrittenBookContent>
        get() = TODO("Not yet implemented")
    override val enchantments = register<Enchantments>(ItemStackDataKeys.ENCHANTMENTS, enchantmentsItemMetaConverter)
    override val entityData: ItemStackDataComponentConverter<EntityData>
        get() = TODO("Not yet implemented")
    override val equippable: ItemStackDataComponentConverter<Equippable> = register<Equippable>(ItemStackDataKeys.EQUIPPABLE, equippableItemMetaConverter)
    override val storedEnchantments = register<Enchantments>(ItemStackDataKeys.STORED_ENCHANTMENTS, enchantmentsItemMetaConverter)
    override val suspiciousStewEffects: ItemStackDataComponentConverter<SuspiciousStewEffects>
        get() = TODO("Not yet implemented")
    override val tool: ItemStackDataComponentConverter<Tool> = register(ItemStackDataKeys.TOOL, toolItemMetaConverter)
    override val tooltipStyle: ItemStackDataComponentConverter<Key> = register(ItemStackDataKeys.TOOLTIP_STYLE, tooltipStyleItemMetaConverter)
    override val tooltipDisplay: ItemStackDataComponentConverter<TooltipDisplay>
        get() = TODO("Not yet implemented")
    override val trim: ItemStackDataComponentConverter<Trim>
        get() = TODO("Not yet implemented")
    override val customName = register<Component>(ItemStackDataKeys.CUSTOM_NAME, customNameItemMetaConverter)
    override val lore = register<ItemLore>(ItemStackDataKeys.ITEM_LORE, itemLoreItemMetaConverter)
    override val canBreak = register<CanBreak>(ItemStackDataKeys.CAN_BREAK, canBreakItemMetaConverter)
    override val canPlaceOn = register<CanPlaceOn>(ItemStackDataKeys.CAN_PLACE_ON, canPlaceOnItemMetaConverter)
    override val dyedColor = register<DyedColor>(ItemStackDataKeys.DYED_COLOR, dyedColorItemMetaConverter)
    override val enchantable: ItemStackDataComponentConverter<Enchantable> = register<Enchantable>(ItemStackDataKeys.ENCHANTABLE, enchantableItemMetaConverter)
    override val attributeModifiers = register<AttributeModifiers>(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersItemMetaConverter)
    override val chargedProjectiles = register<ChargedProjectiles>(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesItemMetaConverter)
    override val consumables: ItemStackDataComponentConverter<Consumable> = register<Consumable>(ItemStackDataKeys.CONSUMABLE, consumableItemMetaConverter)
    override val intangibleProjectile = register<IntangibleProjectile>(ItemStackDataKeys.INTANGIBLE_PROJECTILE, intangibleProjectileItemMetaConverter)
    override val itemModel: ItemStackDataComponentConverter<Key> = register(ItemStackDataKeys.ITEM_MODEL, itemModelItemMetaConverter)
    override val itemName: ItemStackDataComponentConverter<Component> = register(ItemStackDataKeys.ITEM_NAME, itemNameItemMetaConverter)
    override val jukeboxPlayable: ItemStackDataComponentConverter<JukeboxPlayable>
        get() = TODO("Not yet implemented")
    override val lodestoneTracker: ItemStackDataComponentConverter<LodestoneTracker>
        get() = TODO("Not yet implemented")
    override val mapId = register<Int>(ItemStackDataKeys.MAP_ID, mapIdItemMetaConverter)
    override val maxDamage: ItemStackDataComponentConverter<Int> = register(ItemStackDataKeys.MAX_DAMAGE, maxDamageItemMetaConverter)
    override val maxStackSize: ItemStackDataComponentConverter<Int> = register(ItemStackDataKeys.MAX_STACK_SIZE, maxStackSizeItemMetaConverter)
    override val customModelData = register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataItemMetaConverter)
    override val potionContents = register<PotionContents>(ItemStackDataKeys.POTION_CONTENTS, potionContentsItemMetaConverter)
    override val instrument = register<Key>(ItemStackDataKeys.INSTRUMENT, instrumentItemMetaConverter)
    override val recipes = register<List<Key>>(ItemStackDataKeys.RECIPES, recipesItemMetaConverter)
    override val repairable: ItemStackDataComponentConverter<com.wolfyscript.scafall.wrappers.world.items.data.Repairable>
        get() = TODO("Not yet implemented")
    override val fireworkExplosion = register(ItemStackDataKeys.FIREWORK_EXPLOSION, fireworkExplosionItemMetaConverter)
    override val fireworks = register(ItemStackDataKeys.FIREWORKS, fireworksItemMetaConverter)
    override val food = register<Food>(ItemStackDataKeys.FOOD, foodItemMetaConverter)
    override val glider = register(ItemStackDataKeys.GLIDER, gliderItemMetaConverter)
    override val profile = register<Profile>(ItemStackDataKeys.PROFILE, profileItemMetaConverter)
    override val providesBannerPatterns: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
    override val providesTrimMaterial: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
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
    override val mapColor: ItemStackDataComponentConverter<Color>
        get() = TODO("Not yet implemented")
    override val mapDecorations: ItemStackDataComponentConverter<MapDecorations>
        get() = TODO("Not yet implemented")
    override val containerLoot = register<ContainerLoot>(ItemStackDataKeys.CONTAINER_LOOT, containerLootItemMetaConverter)
    override val customData: ItemStackDataComponentConverter<CustomData>
        get() = TODO("Not yet implemented")
    override val blockEntityData = register<BlockEntityData>(ItemStackDataKeys.BLOCK_ENTITY_DATA, blockEntityItemMetaConverter)
    override val blockState = register<BlockState>(ItemStackDataKeys.BLOCK_STATE, blockStateItemMetaConverter)
    override val blocksAttacks: ItemStackDataComponentConverter<BlocksAttacks>
        get() = TODO("Not yet implemented")
    override val bucketEntityData: ItemStackDataComponentConverter<BucketEntityData>
        get() = TODO("Not yet implemented")
    override val enchantmentGlintOverride = register<Boolean>(ItemStackDataKeys.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverrideItemMetaConverter)
    override val bundleContents = register<BundleContents>(ItemStackDataKeys.BUNDLE_CONTENTS, bundleContentsItemMetaConverter)
    override val breakSound: ItemStackDataComponentConverter<SoundEvent>
        get() = TODO("Not yet implemented")

    private inline fun <reified T : Any> register(
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

data class ItemMetaDataKeyConverter<T: Any>(val fetcher: ItemMeta.() -> T?, val applier: ItemMeta.(T?) -> Unit)

class SpigotItemStackDataComponentConverterImpl<T : Any>(
    override val key: Key,
    override val type: KClass<T>,
    reader: ItemMeta.() -> T?,
    writer: ItemMeta.(T?) -> Unit
) : ItemStackDataComponentConverter<T> {

    override val reader: DataComponentConverter.Reader<T, ItemStackLike<*, *>> = Reader(reader)

    override val modifier: DataComponentConverter.Modifier<T, ItemStack> = Modifier(writer)

    class Reader<T: Any>(
        reader: ItemMeta.() -> T?,
    ) : DataComponentConverter.Reader<T, ItemStackLike<*,*>> {

        override val converter: ItemStackLike<*, *>.() -> Result<T?> = {
            val stack = unwrap().itemMeta?.reader()
            Result.success(stack)
        }

    }

    class Modifier<T : Any>(
        writer: ItemMeta.(T?) -> Unit
    ) : DataComponentConverter.Modifier<T, ItemStack> {

        override val converter: ItemStack.(T) -> Result<ItemStack> = {
            val ref = unwrap()
            ref.itemMeta?.let { meta ->
                meta.writer(it)
                ref.itemMeta = meta
            }
            Result.success(this)
        }

        override val remover: ItemStack.() -> Result<Pair<ItemStack, Boolean>> = {
            val ref = unwrap()
            ref.itemMeta?.let { meta ->
                meta.writer(null)
                ref.itemMeta = meta
            }
            Result.success(this to true)
        }

    }
}
