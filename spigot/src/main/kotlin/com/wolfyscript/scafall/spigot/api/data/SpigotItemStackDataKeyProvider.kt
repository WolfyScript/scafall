package com.wolfyscript.scafall.spigot.api.data

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.function.ReceiverBiConsumer
import com.wolfyscript.scafall.function.ReceiverFunction
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.ItemStackImpl
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.*
import kotlin.reflect.KClass

class SpigotItemStackDataKeyProvider(private val scafall: Scafall) : DataKeyProvider {

    private val map: MutableMap<Key, DataKey<*, ItemStack>> = mutableMapOf()

    override val damage: DataKey<Int, ItemStack> = register("damage", damageItemMetaConverter)
    override val repairCost: DataKey<Int, ItemStack> = register<Int>("repair_cost", repairCostItemMetaConverter)
    override val unbreakable: DataKey<Unbreakable, ItemStack> = register<Unbreakable>("unbreakable", unbreakableItemMetaConverter)
    override val enchantments: DataKey<Enchantments, ItemStack> = register<Enchantments>("enchantments", enchantmentsItemMetaConverter)
    override val storedEnchantments: DataKey<Enchantments, ItemStack> = register<Enchantments>("stored_enchantments", enchantmentsItemMetaConverter)
    override val customName: DataKey<Component, ItemStack> = register<Component>("custom_name", displayNameItemMetaConverter)
    override val itemLore: DataKey<ItemLore, ItemStack> = register<ItemLore>("item_lore", itemLoreItemMetaConverter)
    override val canBreak: DataKey<CanBreak, ItemStack> = register<CanBreak>("can_break", canBreakItemMetaConverter)
    override val canPlaceOn: DataKey<CanPlaceOn, ItemStack> = register<CanPlaceOn>("can_place_on", canPlaceOnItemMetaConverter)
    override val dyedColor: DataKey<DyedColor, ItemStack> = register<DyedColor>("dyed_color", dyedColorItemMetaConverter)
    override val attributeModifiers: DataKey<AttributeModifiers, ItemStack> = register<AttributeModifiers>("attribute_modifiers", attributeModifiersItemMetaConverter)
    override val chargedProjectiles: DataKey<ChargedProjectiles, ItemStack> = register<ChargedProjectiles>("charged_projectiles", chargedProjectilesItemMetaConverter)
    override val intangibleProjectiles: DataKey<IntangibleProjectiles, ItemStack> = register<IntangibleProjectiles>("intangible_projectiles", intangibleProjectilesItemMetaConverter)
    // TODO: Map Color
    // TODO: map Decorations
    override val mapId: DataKey<Int, ItemStack> = register<Int>("map_id", mapIdItemMetaConverter)
    // TODO: Map Info
    override val customModelData: DataKey<Int, ItemStack> = register("custom_model_data", customModelDataItemMetaConverter)
    override val potionEffects: DataKey<PotionContents, ItemStack> = register<PotionContents>("potion_contents", potionContentsItemMetaConverter)
    // TODO: Writable Book Contents
    // TODO: Written Book Contents
    // TODO: Trim
    // TODO: Suspicious Stew
    // TODO: Hide Additional Tooltip
    // TODO: Debug Stick State
    // TODO: Entity Data
    // TODO: Bucket Entity Data
    override val instrument: DataKey<Key, ItemStack> = register<Key>("instrument", instrumentItemMetaConverter)
    override val recipes: DataKey<List<Key>, ItemStack> = register<List<Key>>("recipes", recipesItemMetaConverter)
    // TODO: Lodestone Tracker
    override val fireworkExplosion: DataKey<FireworkExplosion, ItemStack> = register("firework_explosion", fireworkExplosionItemMetaConverter)
    override val fireworks: DataKey<Fireworks, ItemStack> = register("fireworks", fireworksItemMetaConverter)
    override val profile: DataKey<Profile, ItemStack> = register<Profile>("profile", profileItemMetaConverter)
    override val noteBlockSound: DataKey<Key, ItemStack> = register<Key>("note_block_sound", noteBlockSoundItemMetaConverter)
    override val baseColor: DataKey<DyeColor, ItemStack> = register<DyeColor>("base_color", baseColorItemMetaConverter)
    override val bannerPatterns: DataKey<BannerPatterns, ItemStack> = register<BannerPatterns>("banner_patterns", bannerPatternsItemMetaConverter)
    override val potDecorations: DataKey<List<Key>, ItemStack> = register<List<Key>>("pot_decorations", potDecorationsItemMetaConverter)
    override val container: DataKey<Container, ItemStack> = register<Container>("container", containerItemMetaConverter)
    override val bees: DataKey<Bees, ItemStack> = register<Bees>("bees", beesItemMetaConverter)
    override val lock: DataKey<Lock, ItemStack> = register<Lock>("lock", lockItemMetaConverter)
    override val containerLoot: DataKey<ContainerLoot, ItemStack> = register<ContainerLoot>("container_loot", containerLootItemMetaConverter)
    override val blockEntityData: DataKey<BlockEntityData, ItemStack> = register<BlockEntityData>("block_entity_data", blockEntityItemMetaConverter)
    override val blockState: DataKey<BlockState, ItemStack> = register<BlockState>("block_state", blockStateItemMetaConverter)
    override val enchantmentGlintOverride: DataKey<Boolean, ItemStack> = register<Boolean>("enchantment_glint_override", enchantmentGlintOverrideItemMetaConverter)
    override val bundleContents: DataKey<BundleContents, ItemStack> = register<BundleContents>("bundle_contents", bundleContentsItemMetaConverter)

    private inline fun <reified T : Any> register(
        key: String,
        converter: ItemMetaDataKeyConverter<T>
    ) : DataKey<T, ItemStack> {
        return register(Key.key(Key.MINECRAFT_NAMESPACE, key), converter.fetcher, converter.applier)
    }

    private inline fun <reified T : Any> register(
        key: String,
        fetcher: ReceiverFunction<ItemMeta, T?>,
        applier: ReceiverBiConsumer<ItemMeta, T>
    ) : DataKey<T, ItemStack> {
        return register(Key.key(Key.MINECRAFT_NAMESPACE, key), fetcher, applier)
    }

    private inline fun <reified T : Any> register(
        key: Key,
        fetcher: ReceiverFunction<ItemMeta, T?>,
        applier: ReceiverBiConsumer<ItemMeta, T>
    ) : DataKey<T, ItemStack> {
        val dataKey = DataKey<T, ItemStack>(T::class, key,
            fetcher = {
                if (this is ItemStackImpl) {
                    return@DataKey bukkitRef?.itemMeta?.let { meta ->
                        with(fetcher) {
                            meta.apply()
                        }
                    }
                }
                null
            },
            applier = { data ->
                if (this is ItemStackImpl) {
                    val meta = bukkitRef?.itemMeta
                    if (meta != null) {
                        with(applier) { meta.consume(data) }
                        bukkitRef?.setItemMeta(meta)
                    }
                }
                this
            })
        map[key] = dataKey
        return dataKey
    }


    override fun <T : Any> getDataKey(
        type: KClass<T>,
        key: Key
    ): DataKey<T, ItemStack> {
        val builder = map[key]
        if (builder != null) {
            if (builder.type != type) {
                throw IllegalArgumentException("Cannot create Builder $key! Invalid value type: Registered Builder contains value of type ${builder.type}, but requested value type was $type")
            }
            @Suppress("UNCHECKED_CAST") // We checked that the key and type is the same, so we can cast it here
            return builder as DataKey<T, ItemStack>
        }

        // TODO: Get logger from plugins
//        scaffolding.wolfyUtils.logger.warning("Cannot create Builder $key! Builder was not registered! Falling back to empty DataKey!")
        return DataKey(type, key, fetcher = { null }, applier = { this })
    }

}