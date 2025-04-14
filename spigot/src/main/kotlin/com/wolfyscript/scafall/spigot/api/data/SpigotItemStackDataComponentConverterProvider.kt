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

class SpigotItemStackDataComponentConverterProvider(private val scafall: Scafall) {

    fun register() {
        register(ItemStackDataKeys.DAMAGE, damageItemMetaConverter)
        register<Int>(ItemStackDataKeys.REPAIR_COST, repairCostItemMetaConverter)
        register<Unbreakable>(ItemStackDataKeys.UNBREAKABLE, unbreakableItemMetaConverter)
        register<Enchantments>(ItemStackDataKeys.ENCHANTMENTS, enchantmentsItemMetaConverter)
        register<Equippable>(ItemStackDataKeys.EQUIPPABLE, equippableItemMetaConverter)
        register<Enchantments>(ItemStackDataKeys.STORED_ENCHANTMENTS, enchantmentsItemMetaConverter)
        register(ItemStackDataKeys.TOOL, toolItemMetaConverter)
        register(ItemStackDataKeys.TOOLTIP_STYLE, tooltipStyleItemMetaConverter)
        register<Component>(ItemStackDataKeys.CUSTOM_NAME, customNameItemMetaConverter)
        register<ItemLore>(ItemStackDataKeys.ITEM_LORE, itemLoreItemMetaConverter)
        register<CanBreak>(ItemStackDataKeys.CAN_BREAK, canBreakItemMetaConverter)
        register<CanPlaceOn>(ItemStackDataKeys.CAN_PLACE_ON, canPlaceOnItemMetaConverter)
        register<DyedColor>(ItemStackDataKeys.DYED_COLOR, dyedColorItemMetaConverter)
        register<Enchantable>(ItemStackDataKeys.ENCHANTABLE, enchantableItemMetaConverter)
        register<AttributeModifiers>(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersItemMetaConverter)
        register<ChargedProjectiles>(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesItemMetaConverter)
        register<Consumable>(ItemStackDataKeys.CONSUMABLE, consumableItemMetaConverter)
        register<IntangibleProjectile>(ItemStackDataKeys.INTANGIBLE_PROJECTILE, intangibleProjectileItemMetaConverter)
        register(ItemStackDataKeys.ITEM_MODEL, itemModelItemMetaConverter)
        register(ItemStackDataKeys.ITEM_NAME, itemNameItemMetaConverter)
        register<Int>(ItemStackDataKeys.MAP_ID, mapIdItemMetaConverter)
        register(ItemStackDataKeys.MAX_DAMAGE, maxDamageItemMetaConverter)
        register(ItemStackDataKeys.MAX_STACK_SIZE, maxStackSizeItemMetaConverter)
        register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataItemMetaConverter)
        register<PotionContents>(ItemStackDataKeys.POTION_CONTENTS, potionContentsItemMetaConverter)
        register<Key>(ItemStackDataKeys.INSTRUMENT, instrumentItemMetaConverter)
        register<List<Key>>(ItemStackDataKeys.RECIPES, recipesItemMetaConverter)
        register(ItemStackDataKeys.FIREWORK_EXPLOSION, fireworkExplosionItemMetaConverter)
        register(ItemStackDataKeys.FIREWORKS, fireworksItemMetaConverter)
        register<Food>(ItemStackDataKeys.FOOD, foodItemMetaConverter)
        register(ItemStackDataKeys.GLIDER, gliderItemMetaConverter)
        register<Profile>(ItemStackDataKeys.PROFILE, profileItemMetaConverter)
        register<Key>(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundItemMetaConverter)
        register<DyeColor>(ItemStackDataKeys.BASE_COLOR, baseColorItemMetaConverter)
        register<BannerPatterns>(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternsItemMetaConverter)
        register<List<Key>>(ItemStackDataKeys.POT_DECORATIONS, potDecorationsItemMetaConverter)
        register<Container>(ItemStackDataKeys.CONTAINER, containerItemMetaConverter)
        register<Bees>(ItemStackDataKeys.BEES, beesItemMetaConverter)
        register<Lock>(ItemStackDataKeys.LOCK, lockItemMetaConverter)
        register<ContainerLoot>(ItemStackDataKeys.CONTAINER_LOOT, containerLootItemMetaConverter)
        register<BlockEntityData>(ItemStackDataKeys.BLOCK_ENTITY_DATA, blockEntityItemMetaConverter)
        register<BlockState>(ItemStackDataKeys.BLOCK_STATE, blockStateItemMetaConverter)
        register<Boolean>(ItemStackDataKeys.ENCHANTMENT_GLINT_OVERRIDE, enchantmentGlintOverrideItemMetaConverter)
        register<BundleContents>(ItemStackDataKeys.BUNDLE_CONTENTS, bundleContentsItemMetaConverter)
    }

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
