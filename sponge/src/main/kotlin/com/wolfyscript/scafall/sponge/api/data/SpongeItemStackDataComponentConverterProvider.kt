package com.wolfyscript.scafall.sponge.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.unwrap
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import kotlin.reflect.KClass

class SpongeItemStackDataComponentConverterProvider {

    fun register() {
        register(ItemDataComponentTypes.DAMAGE, damageConverter)
        register(ItemDataComponentTypes.REPAIR_COST, repairCostConverter)
        register(ItemDataComponentTypes.UNBREAKABLE, unbreakableDataConverter)
        register(ItemDataComponentTypes.ENCHANTMENTS, enchantmentsDataConverter)
        register(ItemDataComponentTypes.STORED_ENCHANTMENTS, enchantmentsDataConverter)
        register(ItemDataComponentTypes.CUSTOM_NAME, displayNameConverter)
        register(ItemDataComponentTypes.ITEM_LORE, displayLoreConverter)
        register(ItemDataComponentTypes.CAN_BREAK, canBreakDataConverter)
        register(ItemDataComponentTypes.CAN_PLACE_ON, canPlaceOnDataConverter)
        register(ItemDataComponentTypes.DYED_COLOR, dyedColorDataConverter)
        register(ItemDataComponentTypes.ATTRIBUTE_MODIFIERS, attributeModifiersDataConverter)
        register(ItemDataComponentTypes.CHARGED_PROJECTILES, chargedProjectilesDataConverter)
        register(ItemDataComponentTypes.INTANGIBLE_PROJECTILE, intangibleProjectileDataConverter)
        register(ItemDataComponentTypes.ITEM_MODEL, itemModelConverter)
        register(ItemDataComponentTypes.ITEM_NAME, itemNameConverter)
        register(ItemDataComponentTypes.MAP_ID, mapIdDataConverter)
        register(ItemDataComponentTypes.CUSTOM_MODEL_DATA, customModelDataConverter)
        register(ItemDataComponentTypes.POTION_CONTENTS, potionContentsDataConverter)
        register(ItemDataComponentTypes.INSTRUMENT, instrumentConverter)
        register(ItemDataComponentTypes.RECIPES, recipesDataConverter)
        register(ItemDataComponentTypes.FIREWORK_EXPLOSION, fireworkExplosionDataConverter)
        register(ItemDataComponentTypes.FIREWORKS, fireworksDataConverter)
        register(ItemDataComponentTypes.PROFILE, profileDataConverter)
        register(ItemDataComponentTypes.NOTE_BLOCK_SOUND, noteBlockSoundConverter)
        register(ItemDataComponentTypes.BASE_COLOR, baseColorDataConverter)
        register(ItemDataComponentTypes.BANNER_PATTERNS, bannerPatternDataConverter)
        register(ItemDataComponentTypes.POT_DECORATIONS, potDecorationsDataConverter)
        register(ItemDataComponentTypes.CONTAINER, containerDataConverter)
        register(ItemDataComponentTypes.BEES, beesDataConverter)
        register(ItemDataComponentTypes.LOCK, lockDataConverter)
        register(ItemDataComponentTypes.CONTAINER_LOOT, containerLootDataConverter)
        register(ItemDataComponentTypes.BLOCK_ENTITY_DATA, blockEntityDataConverter)
        register(ItemDataComponentTypes.BLOCK_STATE, blockStateDataConverter)
        register(ItemDataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchantmentOverrideDataConverter)
        register(ItemDataComponentTypes.BUNDLE_CONTENTS, bundleContentsDataConverter)
    }

    private inline fun <reified T : Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: SpongeItemStackDataComponentConverter<T>
    ): ItemDataComponentConverter<T> {
        val converterImpl = ItemDataComponentConverterImpl(
            dataKey.key(),
            T::class,
            converter.fetcher,
            converter.applier,
            converter.remover
        )
        return converterImpl
    }

}

data class SpongeItemStackDataComponentConverter<T : Any>(
    val fetcher: org.spongepowered.api.item.inventory.ItemStackLike.() -> T?,
    val applier: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit,
    val remover: org.spongepowered.api.item.inventory.ItemStack.() -> Unit = {}
)

class ItemDataComponentConverterImpl<T : Any, H : DataHolder<H, *>>(
    override val key: Key,
    override val type: KClass<T>,
    reader: org.spongepowered.api.item.inventory.ItemStackLike.() -> T?,
    writer: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit,
    remover: org.spongepowered.api.item.inventory.ItemStack.() -> Unit
) : ItemDataComponentConverter<T> {

    override val reader: DataComponentConverter.Reader<T, ItemStackLike<*, *>> = Reader(reader)

    override val modifier: DataComponentConverter.Modifier<T, ItemStack> = Modifier(writer, remover)

    class Reader<T: Any>(
        reader: org.spongepowered.api.item.inventory.ItemStackLike.() -> T?,
    ) : DataComponentConverter.Reader<T, ItemStackLike<*,*>> {

        override val converter: ItemStackLike<*, *>.() -> Result<T?> = {
            val stack = unwrap().reader()
            Result.success(stack)
        }

    }

    class Modifier<T : Any>(
        writer: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit,
        remover: org.spongepowered.api.item.inventory.ItemStack.() -> Unit
    ) : DataComponentConverter.Modifier<T, ItemStack> {

        override val converter: ItemStack.(T) -> Result<ItemStack> = {
            unwrap().writer(it)
            Result.success(this)
        }

        override val remover: ItemStack.() -> Result<Pair<ItemStack, Boolean>> = {
            unwrap().remover()
            Result.success(this to true)
        }

    }
}
