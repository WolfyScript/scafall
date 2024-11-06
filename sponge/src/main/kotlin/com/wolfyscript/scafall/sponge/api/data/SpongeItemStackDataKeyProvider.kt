package com.wolfyscript.scafall.sponge.api.data

import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.unwrap
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.data.*
import net.kyori.adventure.text.Component
import kotlin.reflect.KClass

class SpongeItemStackDataKeyProvider : DataKeyProvider {

    private val map: MutableMap<Key, DataKey<*, ItemStack>> = mutableMapOf()

    override val damage: DataKey<Int, ItemStack> = register("damage", damageConverter)
    override val repairCost: DataKey<Int, ItemStack> = register("repair_cost", repairCostConverter)
    override val unbreakable: DataKey<Unbreakable, ItemStack> = register("unbreakable", unbreakableDataConverter)
    override val enchantments: DataKey<Enchantments, ItemStack> = register("enchantments", enchantmentsDataConverter)
    override val storedEnchantments: DataKey<Enchantments, ItemStack> = register("stored_enchantments", enchantmentsDataConverter)
    override val customName: DataKey<Component, ItemStack> = register("custom_name", displayNameConverter)
    override val itemLore: DataKey<ItemLore, ItemStack> = register("item_lore", displayLoreConverter)
    override val canBreak: DataKey<CanBreak, ItemStack> = register("can_break", canBreakDataConverter)
    override val canPlaceOn: DataKey<CanPlaceOn, ItemStack> = register("can_place_on", canPlaceOnDataConverter)
    override val dyedColor: DataKey<DyedColor, ItemStack> = register("dyed_color", dyedColorDataConverter)
    override val attributeModifiers: DataKey<AttributeModifiers, ItemStack> = register("attribute_modifiers", attributeModifiersDataConverter)
    override val chargedProjectiles: DataKey<ChargedProjectiles, ItemStack> = register("charged_projectiles", chargedProjectilesDataConverter)
    override val intangibleProjectiles: DataKey<IntangibleProjectiles, ItemStack> = register("intangible_projectiles", intangibleProjectilesDataConverter)
    override val mapId: DataKey<Int, ItemStack> = register("map_id", mapIdDataConverter)
    override val customModelData: DataKey<Int, ItemStack> = register("custom_model_data", customModelDataConverter)
    override val potionEffects: DataKey<PotionContents, ItemStack> = register("potion_contents", potionContentsDataConverter)
    override val instrument: DataKey<Key, ItemStack> = register("instrument", instrumentConverter)
    override val recipes: DataKey<List<Key>, ItemStack> = register("recipes", recipesDataConverter)
    override val fireworkExplosion: DataKey<FireworkExplosion, ItemStack> = register("firework_explosions", fireworkExplosionDataConverter)
    override val fireworks: DataKey<Fireworks, ItemStack> = register("fireworks", fireworksDataConverter)
    override val profile: DataKey<Profile, ItemStack> = register("profile", profileDataConverter)
    override val noteBlockSound: DataKey<Key, ItemStack> = register("note_block_sound", noteBlockSoundConverter)
    override val baseColor: DataKey<DyeColor, ItemStack> = register("base_color", baseColorDataConverter)
    override val bannerPatterns: DataKey<BannerPatterns, ItemStack> = register("banner_patterns", bannerPatternDataConverter)
    override val potDecorations: DataKey<List<Key>, ItemStack> = register("pot_decorations", potDecorationsDataConverter)
    override val container: DataKey<Container, ItemStack> = register("container", containerDataConverter)
    override val bees: DataKey<Bees, ItemStack> = register("bees", beesDataConverter)
    override val lock: DataKey<Lock, ItemStack> = register("lock", lockDataConverter)
    override val containerLoot: DataKey<ContainerLoot, ItemStack> = register("container_loot", containerLootDataConverter)
    override val blockEntityData: DataKey<BlockEntityData, ItemStack> = register("block_entity_data", blockEntityDataConverter)
    override val blockState: DataKey<BlockState, ItemStack> = register("block_state", blockStateDataConverter)
    override val enchantmentGlintOverride: DataKey<Boolean, ItemStack> = register("enchantment_glint_override", enchantmentOverrideDataConverter)
    override val bundleContents: DataKey<BundleContents, ItemStack> = register("bundle_contents", bundleContentsDataConverter)

    private inline fun <reified T : Any> register(key: String, converter: ItemStackDataKeyConverter<T>) : DataKey<T, ItemStack> {
        return register(key, converter.fetcher, converter.applier)
    }

    private inline fun <reified T : Any> register(
        key: String,
        crossinline fetcher: org.spongepowered.api.item.inventory.ItemStack.() -> T?,
        crossinline applier: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit
    ) : DataKey<T, ItemStack> {
        return register(Key.key(Key.MINECRAFT_NAMESPACE, key), fetcher, applier)
    }

    private inline fun <reified T : Any> register(
        key: Key,
        crossinline fetcher: org.spongepowered.api.item.inventory.ItemStack.() -> T?,
        crossinline applier: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit
    ) : DataKey<T, ItemStack> {
        val dataKey = DataKey<T, ItemStack>(T::class, key,
            fetcher = { unwrap().fetcher() },
            applier = {
                unwrap().applier(it)
                this
            })
        map[key] = dataKey
        return dataKey
    }


    override fun <T : Any> getDataKey(type: KClass<T>, key: Key): DataKey<T, ItemStack> {
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

data class ItemStackDataKeyConverter<T: Any>(val fetcher: org.spongepowered.api.item.inventory.ItemStack.() -> T?, val applier: org.spongepowered.api.item.inventory.ItemStack.(T) -> Unit)
