package com.wolfyscript.scafall.spigot.api.data

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.*
import kotlin.reflect.KClass

class SpigotItemStackDataComponentConverterProvider(private val scafall: Scafall) {

    fun register() {
        register(ItemDataComponentTypes.DAMAGE, damageItemMetaConverter)
        register<Int>(ItemDataComponentTypes.REPAIR_COST, repairCostItemMetaConverter)
        register<Unbreakable>(ItemDataComponentTypes.UNBREAKABLE, unbreakableItemMetaConverter)
        register<Enchantments>(ItemDataComponentTypes.ENCHANTMENTS, enchantmentsItemMetaConverter)
        register<Equippable>(ItemDataComponentTypes.EQUIPPABLE, equippableItemMetaConverter)
        register<Enchantments>(ItemDataComponentTypes.STORED_ENCHANTMENTS, enchantmentsItemMetaConverter)
        register(ItemDataComponentTypes.TOOL, toolItemMetaConverter)
        register(ItemDataComponentTypes.TOOLTIP_STYLE, tooltipStyleItemMetaConverter)
        register<Component>(ItemDataComponentTypes.CUSTOM_NAME, customNameItemMetaConverter)
        register<ItemLore>(ItemDataComponentTypes.ITEM_LORE, itemLoreItemMetaConverter)
        register<CanBreak>(ItemDataComponentTypes.CAN_BREAK, canBreakItemMetaConverter)
        register<CanPlaceOn>(ItemDataComponentTypes.CAN_PLACE_ON, canPlaceOnItemMetaConverter)
        register<DyedColor>(ItemDataComponentTypes.DYED_COLOR, dyedColorItemMetaConverter)
        register<Enchantable>(ItemDataComponentTypes.ENCHANTABLE, enchantableItemMetaConverter)
        register<ChargedProjectiles>(ItemDataComponentTypes.CHARGED_PROJECTILES, chargedProjectilesItemMetaConverter)
        register<Consumable>(ItemDataComponentTypes.CONSUMABLE, consumableItemMetaConverter)
        register<IntangibleProjectile>(ItemDataComponentTypes.INTANGIBLE_PROJECTILE, intangibleProjectileItemMetaConverter)
        register(ItemDataComponentTypes.ITEM_MODEL, itemModelItemMetaConverter)
        register(ItemDataComponentTypes.ITEM_NAME, itemNameItemMetaConverter)
        register<Int>(ItemDataComponentTypes.MAP_ID, mapIdItemMetaConverter)
        register(ItemDataComponentTypes.MAX_DAMAGE, maxDamageItemMetaConverter)
        register(ItemDataComponentTypes.MAX_STACK_SIZE, maxStackSizeItemMetaConverter)
        register(ItemDataComponentTypes.CUSTOM_MODEL_DATA, customModelDataItemMetaConverter)
        register<PotionContents>(ItemDataComponentTypes.POTION_CONTENTS, potionContentsItemMetaConverter)
        register<Key>(ItemDataComponentTypes.INSTRUMENT, instrumentItemMetaConverter)
        register<List<Key>>(ItemDataComponentTypes.RECIPES, recipesItemMetaConverter)
        register(ItemDataComponentTypes.FIREWORK_EXPLOSION, fireworkExplosionItemMetaConverter)
        register(ItemDataComponentTypes.FIREWORKS, fireworksItemMetaConverter)
        register<Food>(ItemDataComponentTypes.FOOD, foodItemMetaConverter)
        register(ItemDataComponentTypes.GLIDER, gliderItemMetaConverter)
        register<Profile>(ItemDataComponentTypes.PROFILE, profileItemMetaConverter)
        register<Key>(ItemDataComponentTypes.NOTE_BLOCK_SOUND, noteBlockSoundItemMetaConverter)
        register<DyeColor>(ItemDataComponentTypes.BASE_COLOR, baseColorItemMetaConverter)
        register<BannerPatterns>(ItemDataComponentTypes.BANNER_PATTERNS, bannerPatternsItemMetaConverter)
        register<List<Key>>(ItemDataComponentTypes.POT_DECORATIONS, potDecorationsItemMetaConverter)
        register<Container>(ItemDataComponentTypes.CONTAINER, containerItemMetaConverter)
        register<Bees>(ItemDataComponentTypes.BEES, beesItemMetaConverter)
        register<Lock>(ItemDataComponentTypes.LOCK, lockItemMetaConverter)
        register<ContainerLoot>(ItemDataComponentTypes.CONTAINER_LOOT, containerLootItemMetaConverter)
        register<BlockEntityData>(ItemDataComponentTypes.BLOCK_ENTITY_DATA, blockEntityItemMetaConverter)
        register<BlockState>(ItemDataComponentTypes.BLOCK_STATE, blockStateItemMetaConverter)
        register<Boolean>(ItemDataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverrideItemMetaConverter)
        register<BundleContents>(ItemDataComponentTypes.BUNDLE_CONTENTS, bundleContentsItemMetaConverter)
    }

    private inline fun <reified T : Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: ItemMetaDataKeyConverter<T>
    ) : ItemDataComponentConverter<T> {
        if (scafall.registries.itemDataComponentConverterRegistry[dataKey.key] != null) {
            return scafall.registries.itemDataComponentConverterRegistry[dataKey.key] as ItemDataComponentConverter<T>
        }
        val converterImpl = SpigotItemDataComponentConverterImpl(
            dataKey.key,
            T::class,
            converter.fetcher,
            converter.applier
        )
        ScafallProvider.get().registries.itemDataComponentConverterRegistry.register(dataKey.key, converterImpl)
        return converterImpl
    }

}

data class ItemMetaDataKeyConverter<T: Any>(val fetcher: ItemMeta.() -> T?, val applier: ItemMeta.(T?) -> Unit)

class SpigotItemDataComponentConverterImpl<T : Any>(
    override val key: Key,
    override val type: KClass<T>,
    reader: ItemMeta.() -> T?,
    writer: ItemMeta.(T?) -> Unit
) : ItemDataComponentConverter<T> {

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
