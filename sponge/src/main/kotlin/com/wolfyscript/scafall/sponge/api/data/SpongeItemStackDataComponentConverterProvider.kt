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
import com.wolfyscript.scafall.wrappers.world.items.data.Trim
import com.wolfyscript.scafall.wrappers.world.items.data.UseCooldown
import com.wolfyscript.scafall.wrappers.world.items.data.UseRemainder
import com.wolfyscript.scafall.wrappers.world.items.data.WriteableBookContent
import com.wolfyscript.scafall.wrappers.world.items.data.WrittenBookContent
import net.kyori.adventure.text.Component
import kotlin.reflect.KClass

class SpongeItemStackDataComponentConverterProvider : DataComponentConverterProvider {

    override val damage = register(ItemStackDataKeys.DAMAGE, damageConverter)
    override val damageResistant: ItemStackDataComponentConverter<DamageResistant>
        get() = TODO("Not yet implemented")
    override val debugStickState: ItemStackDataComponentConverter<DebugStickState>
        get() = TODO("Not yet implemented")
    override val deathProtection: ItemStackDataComponentConverter<DeathProtection>
        get() = TODO("Not yet implemented")
    override val repairCost = register(ItemStackDataKeys.REPAIR_COST, repairCostConverter)
    override val unbreakable = register(ItemStackDataKeys.UNBREAKABLE, unbreakableDataConverter)
    override val useCooldown: ItemStackDataComponentConverter<UseCooldown>
        get() = TODO("Not yet implemented")
    override val useRemainder: ItemStackDataComponentConverter<UseRemainder>
        get() = TODO("Not yet implemented")
    override val writableBookContent: ItemStackDataComponentConverter<WriteableBookContent>
        get() = TODO("Not yet implemented")
    override val writtenBookContent: ItemStackDataComponentConverter<WrittenBookContent>
        get() = TODO("Not yet implemented")
    override val enchantments = register(ItemStackDataKeys.ENCHANTMENTS, enchantmentsDataConverter)
    override val entityData: ItemStackDataComponentConverter<EntityData>
        get() = TODO("Not yet implemented")
    override val equippable: ItemStackDataComponentConverter<Equippable>
        get() = TODO("Not yet implemented")
    override val storedEnchantments = register(ItemStackDataKeys.STORED_ENCHANTMENTS, enchantmentsDataConverter)
    override val suspiciousStewEffects: ItemStackDataComponentConverter<SuspiciousStewEffects>
        get() = TODO("Not yet implemented")
    override val tool: ItemStackDataComponentConverter<Tool>
        get() = TODO("Not yet implemented")
    override val tooltipStyle: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
    override val trim: ItemStackDataComponentConverter<Trim>
        get() = TODO("Not yet implemented")
    override val customName = register(ItemStackDataKeys.CUSTOM_NAME, displayNameConverter)
    override val lore = register(ItemStackDataKeys.ITEM_LORE, displayLoreConverter)
    override val canBreak = register(ItemStackDataKeys.CAN_BREAK, canBreakDataConverter)
    override val canPlaceOn = register(ItemStackDataKeys.CAN_PLACE_ON, canPlaceOnDataConverter)
    override val dyedColor = register(ItemStackDataKeys.DYED_COLOR, dyedColorDataConverter)
    override val enchantable: ItemStackDataComponentConverter<Enchantable>
        get() = TODO("Not yet implemented")
    override val attributeModifiers = register(ItemStackDataKeys.ATTRIBUTE_MODIFIERS, attributeModifiersDataConverter)
    override val chargedProjectiles = register(ItemStackDataKeys.CHARGED_PROJECTILES, chargedProjectilesDataConverter)
    override val consumables: ItemStackDataComponentConverter<Consumable>
        get() = TODO("Not yet implemented")
    override val intangibleProjectile = register(ItemStackDataKeys.INTANGIBLE_PROJECTILE, intangibleProjectileDataConverter)
    override val itemModel: ItemStackDataComponentConverter<Key> = register(ItemStackDataKeys.ITEM_MODEL, itemModelConverter)
    override val itemName: ItemStackDataComponentConverter<Component> = register(ItemStackDataKeys.ITEM_NAME, itemNameConverter)
    override val jukeboxPlayable: ItemStackDataComponentConverter<JukeboxPlayable>
        get() = TODO("Not yet implemented")
    override val lodestoneTracker: ItemStackDataComponentConverter<LodestoneTracker>
        get() = TODO("Not yet implemented")
    override val mapId = register(ItemStackDataKeys.MAP_ID, mapIdDataConverter)
    override val maxDamage: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val maxStackSize: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val customModelData = register(ItemStackDataKeys.CUSTOM_MODEL_DATA, customModelDataConverter)
    override val potionContents = register(ItemStackDataKeys.POTION_CONTENTS, potionContentsDataConverter)
    override val instrument = register(ItemStackDataKeys.INSTRUMENT, instrumentConverter)
    override val recipes = register(ItemStackDataKeys.RECIPES, recipesDataConverter)
    override val repairable: ItemStackDataComponentConverter<Repairable>
        get() = TODO("Not yet implemented")
    override val fireworkExplosion = register(ItemStackDataKeys.FIREWORK_EXPLOSION, fireworkExplosionDataConverter)
    override val fireworks = register(ItemStackDataKeys.FIREWORKS, fireworksDataConverter)
    override val food: ItemStackDataComponentConverter<Food>
        get() = TODO("Not yet implemented")
    override val glider: ItemStackDataComponentConverter<Glider>
        get() = TODO("Not yet implemented")
    override val hideAdditionalTooltip: ItemStackDataComponentConverter<HideAdditionalTooltip>
        get() = TODO("Not yet implemented")
    override val hideTooltip: ItemStackDataComponentConverter<HideTooltip> = register(ItemStackDataKeys.HIDE_TOOLTIP, hideTooltipConverter)
    override val profile = register(ItemStackDataKeys.PROFILE, profileDataConverter)
    override val rarity: ItemStackDataComponentConverter<Rarity>
        get() = TODO("Not yet implemented")
    override val noteBlockSound = register(ItemStackDataKeys.NOTE_BLOCK_SOUND, noteBlockSoundConverter)
    override val ominousBottleAmplifier: ItemStackDataComponentConverter<OminousBottleAmplifier>
        get() = TODO("Not yet implemented")
    override val baseColor = register(ItemStackDataKeys.BASE_COLOR, baseColorDataConverter)
    override val bannerPatterns = register(ItemStackDataKeys.BANNER_PATTERNS, bannerPatternDataConverter)
    override val potDecorations = register(ItemStackDataKeys.POT_DECORATIONS, potDecorationsDataConverter)
    override val container = register(ItemStackDataKeys.CONTAINER, containerDataConverter)
    override val bees = register(ItemStackDataKeys.BEES, beesDataConverter)
    override val lock = register(ItemStackDataKeys.LOCK, lockDataConverter)
    override val mapColor: ItemStackDataComponentConverter<Color>
        get() = TODO("Not yet implemented")
    override val mapDecorations: ItemStackDataComponentConverter<MapDecorations>
        get() = TODO("Not yet implemented")
    override val containerLoot = register(ItemStackDataKeys.CONTAINER_LOOT, containerLootDataConverter)
    override val customData: ItemStackDataComponentConverter<CustomData>
        get() = TODO("Not yet implemented")
    override val blockEntityData = register(ItemStackDataKeys.BLOCK_ENTITY_DATA, blockEntityDataConverter)
    override val blockState = register(ItemStackDataKeys.BLOCK_STATE, blockStateDataConverter)
    override val bucketEntityData: ItemStackDataComponentConverter<BucketEntityData>
        get() = TODO("Not yet implemented")
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
