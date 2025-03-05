package com.wolfyscript.scafall.spigot.api.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentConverter
import com.wolfyscript.scafall.data.DataComponentConverterProvider
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.data.ItemStackDataComponentConverter
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.MapColor
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.ItemMeta
import kotlin.reflect.KClass

class PaperItemStackDataComponentConverter : DataComponentConverterProvider {
    override val attributeModifiers: ItemStackDataComponentConverter<AttributeModifiers>
        get() = TODO("Not yet implemented")
    override val bannerPatterns: ItemStackDataComponentConverter<BannerPatterns>
        get() = TODO("Not yet implemented")
    override val baseColor: ItemStackDataComponentConverter<DyeColor>
        get() = TODO("Not yet implemented")
    override val bees: ItemStackDataComponentConverter<Bees>
        get() = TODO("Not yet implemented")
    override val blockEntityData: ItemStackDataComponentConverter<BlockEntityData>
        get() = TODO("Not yet implemented")
    override val blockState: ItemStackDataComponentConverter<BlockState>
        get() = TODO("Not yet implemented")
    override val bucketEntityData: ItemStackDataComponentConverter<BucketEntityData>
        get() = TODO("Not yet implemented")
    override val bundleContents: ItemStackDataComponentConverter<BundleContents>
        get() = TODO("Not yet implemented")
    override val canBreak: ItemStackDataComponentConverter<CanBreak>
        get() = TODO("Not yet implemented")
    override val canPlaceOn: ItemStackDataComponentConverter<CanPlaceOn>
        get() = TODO("Not yet implemented")
    override val chargedProjectiles: ItemStackDataComponentConverter<ChargedProjectiles>
        get() = TODO("Not yet implemented")
    override val consumables: ItemStackDataComponentConverter<Consumable>
        get() = TODO("Not yet implemented")
    override val container: ItemStackDataComponentConverter<Container>
        get() = TODO("Not yet implemented")
    override val containerLoot: ItemStackDataComponentConverter<ContainerLoot>
        get() = TODO("Not yet implemented")
    override val customData: ItemStackDataComponentConverter<CustomData>
        get() = TODO("Not yet implemented")
    override val customModelData: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val customName: ItemStackDataComponentConverter<Component>
        get() = TODO("Not yet implemented")
    override val damage: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val damageResistant: ItemStackDataComponentConverter<DamageResistant>
        get() = TODO("Not yet implemented")
    override val debugStickState: ItemStackDataComponentConverter<DebugStickState>
        get() = TODO("Not yet implemented")
    override val deathProtection: ItemStackDataComponentConverter<DeathProtection>
        get() = TODO("Not yet implemented")
    override val dyedColor: ItemStackDataComponentConverter<DyedColor>
        get() = TODO("Not yet implemented")
    override val enchantable: ItemStackDataComponentConverter<Enchantable>
        get() = TODO("Not yet implemented")
    override val enchantmentGlintOverride: ItemStackDataComponentConverter<Boolean>
        get() = TODO("Not yet implemented")
    override val enchantments: ItemStackDataComponentConverter<Enchantments>
        get() = TODO("Not yet implemented")
    override val entityData: ItemStackDataComponentConverter<EntityData>
        get() = TODO("Not yet implemented")
    override val equippable: ItemStackDataComponentConverter<Equippable>
        get() = TODO("Not yet implemented")
    override val fireworkExplosion: ItemStackDataComponentConverter<FireworkExplosion>
        get() = TODO("Not yet implemented")
    override val fireworks: ItemStackDataComponentConverter<Fireworks>
        get() = TODO("Not yet implemented")
    override val food: ItemStackDataComponentConverter<Food>
        get() = TODO("Not yet implemented")
    override val glider: ItemStackDataComponentConverter<Glider>
        get() = TODO("Not yet implemented")
    override val hideAdditionalTooltip: ItemStackDataComponentConverter<HideAdditionalTooltip>
        get() = TODO("Not yet implemented")
    override val hideTooltip: ItemStackDataComponentConverter<HideTooltip>
        get() = TODO("Not yet implemented")
    override val instrument: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
    override val intangibleProjectiles: ItemStackDataComponentConverter<IntangibleProjectiles>
        get() = TODO("Not yet implemented")
    override val itemModel: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
    override val itemName: ItemStackDataComponentConverter<Component>
        get() = TODO("Not yet implemented")
    override val jukeboxPlayable: ItemStackDataComponentConverter<JukeboxPlayable>
        get() = TODO("Not yet implemented")
    override val lodestoneTracker: ItemStackDataComponentConverter<LodestoneTracker>
        get() = TODO("Not yet implemented")
    override val lore: ItemStackDataComponentConverter<ItemLore>
        get() = TODO("Not yet implemented")
    override val lock: ItemStackDataComponentConverter<Lock>
        get() = TODO("Not yet implemented")
    override val mapColor: ItemStackDataComponentConverter<MapColor>
        get() = TODO("Not yet implemented")
    override val mapDecorations: ItemStackDataComponentConverter<MapDecorations>
        get() = TODO("Not yet implemented")
    override val mapId: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val maxDamage: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val maxStackSize: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val noteBlockSound: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
    override val ominousBottleAmplifier: ItemStackDataComponentConverter<OminousBottleAmplifier>
        get() = TODO("Not yet implemented")
    override val potDecorations: ItemStackDataComponentConverter<List<Key>>
        get() = TODO("Not yet implemented")
    override val potionContents: ItemStackDataComponentConverter<PotionContents>
        get() = TODO("Not yet implemented")
    override val profile: ItemStackDataComponentConverter<Profile>
        get() = TODO("Not yet implemented")
    override val rarity: ItemStackDataComponentConverter<Rarity>
        get() = TODO("Not yet implemented")
    override val recipes: ItemStackDataComponentConverter<List<Key>>
        get() = TODO("Not yet implemented")
    override val repairable: ItemStackDataComponentConverter<Repairable>
        get() = TODO("Not yet implemented")
    override val repairCost: ItemStackDataComponentConverter<Int>
        get() = TODO("Not yet implemented")
    override val storedEnchantments: ItemStackDataComponentConverter<Enchantments>
        get() = TODO("Not yet implemented")
    override val suspiciousStewEffects: ItemStackDataComponentConverter<SuspiciousStewEffects>
        get() = TODO("Not yet implemented")
    override val tool: ItemStackDataComponentConverter<Tool>
        get() = TODO("Not yet implemented")
    override val tooltipStyle: ItemStackDataComponentConverter<Key>
        get() = TODO("Not yet implemented")
    override val trim: ItemStackDataComponentConverter<Trim>
        get() = TODO("Not yet implemented")
    override val unbreakable: ItemStackDataComponentConverter<Unbreakable>
        get() = TODO("Not yet implemented")
    override val useCooldown: ItemStackDataComponentConverter<UseCooldown>
        get() = TODO("Not yet implemented")
    override val useRemainder: ItemStackDataComponentConverter<UseRemainder>
        get() = TODO("Not yet implemented")
    override val writableBookContent: ItemStackDataComponentConverter<WriteableBookContent>
        get() = TODO("Not yet implemented")
    override val writtenBookContent: ItemStackDataComponentConverter<WrittenBookContent>
        get() = TODO("Not yet implemented")

    private inline fun <reified T: Any> register(
        dataKey: DataKey<T, ItemStackLike<*, *>>,
        converter: PaperDataAPIConverter<T>
    ) : ItemStackDataComponentConverter<T> {
        val converterImpl = PaperItemStackDataComponentConverterImpl(dataKey.key(), T::class, converter);
        ScafallProvider.get().registries.itemStackDataComponentConverterRegistry.register(dataKey.key(), converterImpl)
        return converterImpl
    }

    private inline fun <reified T : Any> registerSpigot(
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

class PaperItemStackDataComponentConverterImpl<T : Any>(
    override val key: Key,
    override val type: KClass<T>,
    converter: PaperDataAPIConverter<T>,
) : ItemStackDataComponentConverter<T> {

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
    val modifierConverter: ItemStack.(T?) -> Result<ItemStack>,
    val modifierRemover: ItemStack.() -> Result<Pair<ItemStack, Boolean>>,
)