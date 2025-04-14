package com.wolfyscript.scafall.sponge.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.*
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.unwrap
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.Color
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.MapColor
import com.wolfyscript.scafall.wrappers.world.items.data.BlocksAttacks
import com.wolfyscript.scafall.wrappers.world.items.data.BucketEntityData
import com.wolfyscript.scafall.wrappers.world.items.data.Consumable
import com.wolfyscript.scafall.wrappers.world.items.data.CustomData
import com.wolfyscript.scafall.wrappers.world.items.data.DamageResistant
import com.wolfyscript.scafall.wrappers.world.items.data.DeathProtection
import com.wolfyscript.scafall.wrappers.world.items.data.DebugStickState
import com.wolfyscript.scafall.wrappers.world.items.data.Enchantable
import com.wolfyscript.scafall.wrappers.world.items.data.EntityData
import com.wolfyscript.scafall.wrappers.world.items.data.Equippable
import com.wolfyscript.scafall.wrappers.world.items.data.Food
import com.wolfyscript.scafall.wrappers.world.items.data.Glider
import com.wolfyscript.scafall.wrappers.world.items.data.HideAdditionalTooltip
import com.wolfyscript.scafall.wrappers.world.items.data.HideTooltip
import com.wolfyscript.scafall.wrappers.world.items.data.JukeboxPlayable
import com.wolfyscript.scafall.wrappers.world.items.data.LodestoneTracker
import com.wolfyscript.scafall.wrappers.world.items.data.MapDecorations
import com.wolfyscript.scafall.wrappers.world.items.data.OminousBottleAmplifier
import com.wolfyscript.scafall.wrappers.world.items.data.Rarity
import com.wolfyscript.scafall.wrappers.world.items.data.Repairable
import com.wolfyscript.scafall.wrappers.world.items.data.SuspiciousStewEffects
import com.wolfyscript.scafall.wrappers.world.items.data.Tool
import com.wolfyscript.scafall.wrappers.world.items.data.TooltipDisplay
import com.wolfyscript.scafall.wrappers.world.items.data.Trim
import com.wolfyscript.scafall.wrappers.world.items.data.UseCooldown
import com.wolfyscript.scafall.wrappers.world.items.data.UseRemainder
import com.wolfyscript.scafall.wrappers.world.items.data.Weapon
import com.wolfyscript.scafall.wrappers.world.items.data.WriteableBookContent
import com.wolfyscript.scafall.wrappers.world.items.data.WrittenBookContent
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import net.kyori.adventure.text.Component
import kotlin.reflect.KClass

class SpongeItemStackDataComponentConverterProvider {

    fun register() {
        register(ItemStackDataKeys.DAMAGE, damageConverter)
        register(ItemStackDataKeys.REPAIR_COST, repairCostConverter)
        register(ItemStackDataKeys.UNBREAKABLE, unbreakableDataConverter)
        register(ItemStackDataKeys.ENCHANTMENTS, enchantmentsDataConverter)
        register(ItemStackDataKeys.STORED_ENCHANTMENTS, enchantmentsDataConverter)
        register(ItemStackDataKeys.CUSTOM_NAME, displayNameConverter)
        register(ItemStackDataKeys.ITEM_LORE, displayLoreConverter)
        register(ItemStackDataKeys.CAN_BREAK, canBreakDataConverter)
        register(ItemStackDataKeys.CAN_PLACE_ON, canPlaceOnDataConverter)
        register(ItemStackDataKeys.DYED_COLOR, dyedColorDataConverter)
        register(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersDataConverter)
        register(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesDataConverter)
        register(ItemStackDataKeys.INTANGIBLE_PROJECTILE, intangibleProjectileDataConverter)
        register(ItemStackDataKeys.ITEM_MODEL, itemModelConverter)
        register(ItemStackDataKeys.ITEM_NAME, itemNameConverter)
        register(ItemStackDataKeys.MAP_ID, mapIdDataConverter)
        register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataConverter)
        register(ItemStackDataKeys.POTION_CONTENTS, potionContentsDataConverter)
        register(ItemStackDataKeys.INSTRUMENT, instrumentConverter)
        register(ItemStackDataKeys.RECIPES, recipesDataConverter)
        register(ItemStackDataKeys.FIREWORK_EXPLOSION, fireworkExplosionDataConverter)
        register(ItemStackDataKeys.FIREWORKS, fireworksDataConverter)
        register(ItemStackDataKeys.PROFILE, profileDataConverter)
        register(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundConverter)
        register(ItemStackDataKeys.BASE_COLOR, baseColorDataConverter)
        register(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternDataConverter)
        register(ItemStackDataKeys.POT_DECORATIONS, potDecorationsDataConverter)
        register(ItemStackDataKeys.CONTAINER, containerDataConverter)
        register(ItemStackDataKeys.BEES, beesDataConverter)
        register(ItemStackDataKeys.LOCK, lockDataConverter)
        register(ItemStackDataKeys.CONTAINER_LOOT, containerLootDataConverter)
        register(ItemStackDataKeys.BLOCK_ENTITY_DATA, blockEntityDataConverter)
        register(ItemStackDataKeys.BLOCK_STATE, blockStateDataConverter)
        register(ItemStackDataKeys.ENCHANTMENT_GLINT_OVERRIDE, enchantmentOverrideDataConverter)
        register(ItemStackDataKeys.BUNDLE_CONTENTS, bundleContentsDataConverter)
    }

    private inline fun <reified T : Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: SpongeItemStackDataComponentConverter<T>
    ): ItemStackDataComponentConverter<T> {
        val converterImpl = ItemStackDataComponentConverterImpl(
            dataKey.key(),
            T::class,
            converter.fetcher,
            converter.applier,
            converter.remover
        )
        ScafallProvider.get().registries.itemStackDataComponentConverterRegistry.register(dataKey.key(), converterImpl)
        return converterImpl
    }

}

data class SpongeItemStackDataComponentConverter<T : Any>(
    val fetcher: org.spongepowered.api.item.inventory.ItemStackLike.() -> T?,
    val applier: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit,
    val remover: org.spongepowered.api.item.inventory.ItemStack.() -> Unit = {}
)

class ItemStackDataComponentConverterImpl<T : Any, H : DataHolder<H, *>>(
    override val key: Key,
    override val type: KClass<T>,
    reader: org.spongepowered.api.item.inventory.ItemStackLike.() -> T?,
    writer: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit,
    remover: org.spongepowered.api.item.inventory.ItemStack.() -> Unit
) : ItemStackDataComponentConverter<T> {

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
