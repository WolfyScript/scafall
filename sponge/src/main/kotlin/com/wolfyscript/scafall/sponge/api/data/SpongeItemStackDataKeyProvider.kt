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
    override val repairCost: DataKey<Int, ItemStack> = TODO("Not yet implemented")
    override val unbreakable: DataKey<Unbreakable, ItemStack> = register("unbreakable", unbreakableDataConverter)
    override val enchantments: DataKey<Enchantments, ItemStack> = TODO("Not yet implemented")
    override val storedEnchantments: DataKey<Enchantments, ItemStack> = TODO("Not yet implemented")
    override val customName: DataKey<Component, ItemStack> = register("custom_name", displayNameConverter)
    override val itemLore: DataKey<ItemLore, ItemStack> = register("item_lore", displayLoreConverter)
    override val canBreak: DataKey<CanBreak, ItemStack> = TODO("Not yet implemented")
    override val canPlaceOn: DataKey<CanPlaceOn, ItemStack> = TODO("Not yet implemented")
    override val dyedColor: DataKey<DyedColor, ItemStack> = TODO("Not yet implemented")
    override val attributeModifiers: DataKey<AttributeModifiers, ItemStack> = TODO("Not yet implemented")
    override val chargedProjectiles: DataKey<ChargedProjectiles, ItemStack> = TODO("Not yet implemented")
    override val intangibleProjectiles: DataKey<IntangibleProjectiles, ItemStack> = TODO("Not yet implemented")
    override val mapId: DataKey<Int, ItemStack> = TODO("Not yet implemented")
    override val customModelData: DataKey<Int, ItemStack> = register("custom_model_data", customModelDataConverter)
    override val potionEffects: DataKey<PotionContents, ItemStack> = TODO("Not yet implemented")
    override val instrument: DataKey<Key, ItemStack> = TODO("Not yet implemented")
    override val recipes: DataKey<List<Key>, ItemStack> = TODO("Not yet implemented")
    override val fireworkExplosion: DataKey<FireworkExplosion, ItemStack> = TODO("Not yet implemented")
    override val fireworks: DataKey<Fireworks, ItemStack> = TODO("Not yet implemented")
    override val profile: DataKey<Profile, ItemStack> = TODO("Not yet implemented")
    override val noteBlockSound: DataKey<Key, ItemStack> = TODO("Not yet implemented")
    override val baseColor: DataKey<DyeColor, ItemStack> = TODO("Not yet implemented")
    override val bannerPatterns: DataKey<BannerPatterns, ItemStack> = TODO("Not yet implemented")
    override val potDecorations: DataKey<List<Key>, ItemStack> = TODO("Not yet implemented")
    override val container: DataKey<Container, ItemStack> = TODO("Not yet implemented")
    override val bees: DataKey<Bees, ItemStack> = TODO("Not yet implemented")
    override val lock: DataKey<String, ItemStack> = TODO("Not yet implemented")
    override val containerLoot: DataKey<ContainerLoot, ItemStack> = TODO("Not yet implemented")
    override val blockEntityData: DataKey<BlockEntityData, ItemStack> = TODO("Not yet implemented")
    override val blockState: DataKey<BlockState, ItemStack> = TODO("Not yet implemented")
    override val enchantmentGlintOverride: DataKey<Boolean, ItemStack> = TODO("Not yet implemented")
    override val bundleContents: DataKey<BundleContents, ItemStack> = TODO("Not yet implemented")

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
