package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.Color
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.data.CustomModelData
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import com.wolfyscript.scafall.wrappers.world.items.data.Unbreakable
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.data.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.data.type.InstrumentType
import org.spongepowered.api.data.type.InstrumentTypes
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

// durability
val unbreakableDataConverter = SpongeItemStackDataComponentConverter({
    if (get(Keys.IS_UNBREAKABLE).getOrNull() == true) {
        return@SpongeItemStackDataComponentConverter Unbreakable(get(Keys.HIDE_UNBREAKABLE).getOrNull() ?: false)
    }
    null
}, {
    offer(Keys.IS_UNBREAKABLE, true)
    offer(Keys.HIDE_UNBREAKABLE, it.showInTooltip)
}, {
    remove(Keys.IS_UNBREAKABLE)
    remove(Keys.HIDE_UNBREAKABLE)
})
val damageConverter = SpongeItemStackDataComponentConverter({
    get(Keys.ITEM_DURABILITY).map {
        get(Keys.MAX_DURABILITY).getOrElse { 0 } - it
    }.getOrNull()
}, { damage ->
    get(Keys.MAX_DURABILITY).ifPresent { max ->
        offer(Keys.ITEM_DURABILITY, max - damage)
    }
}, {
    offer(Keys.ITEM_DURABILITY, get(Keys.MAX_DURABILITY).getOrElse { 0 })
})
val repairCostConverter = SpongeItemStackDataComponentConverter({
    get(Keys.REPAIR_COST).getOrNull()
}, {
    offer(Keys.REPAIR_COST, it)
}, {
    remove(Keys.REPAIR_COST)
})

// item display options
val displayNameConverter = SpongeItemStackDataComponentConverter({ get(Keys.CUSTOM_NAME).getOrNull() }, {
    offer(Keys.CUSTOM_NAME, it)
}, {
    remove(Keys.CUSTOM_NAME)
})
val displayLoreConverter = SpongeItemStackDataComponentConverter({ ItemLore(get(Keys.LORE).getOrElse { emptyList() }) }, { offer(Keys.LORE, it.lines) })
val customModelDataConverter = SpongeItemStackDataComponentConverter({
    CustomModelData(
        get(Keys.CUSTOM_MODEL_DATA_FLOATS).orElseGet { emptyList<Float>() },
        get(Keys.CUSTOM_MODEL_DATA_FLAGS).orElseGet { emptyList<Boolean>() },
        get(Keys.CUSTOM_MODEL_DATA_STRINGS).orElseGet { emptyList<String>() },
        get(Keys.CUSTOM_MODEL_DATA_COLORS).map { it.map { Color.fromRGB(it.rgb()) } }.orElseGet { emptyList<Color>() }
    )
}, {
    offer(Keys.CUSTOM_MODEL_DATA_FLOATS, it.floats)
    offer(Keys.CUSTOM_MODEL_DATA_FLAGS, it.flags)
    offer(Keys.CUSTOM_MODEL_DATA_STRINGS, it.strings)
    offer(Keys.CUSTOM_MODEL_DATA_COLORS, it.colors.map { org.spongepowered.api.util.Color.ofRgb(it.rgb) })
}, {
    remove(Keys.CUSTOM_MODEL_DATA_FLOATS)
    remove(Keys.CUSTOM_MODEL_DATA_FLAGS)
    remove(Keys.CUSTOM_MODEL_DATA_STRINGS)
    remove(Keys.CUSTOM_MODEL_DATA_COLORS)
})

// Note blocks
val instrumentConverter = SpongeItemStackDataComponentConverter<Key>({
    get(Keys.INSTRUMENT_TYPE).map {
        InstrumentTypes.registry().valueKey(it).toAPI()
    }.getOrNull()
}, {
    InstrumentTypes.registry().findValue<InstrumentType>(ResourceKey.resolve(it.toString())).ifPresent { instrument ->
        offer(Keys.INSTRUMENT_TYPE, instrument)
    }
}, {
    remove(Keys.INSTRUMENT_TYPE)
})
val noteBlockSoundConverter = SpongeItemStackDataComponentConverter({
    get(Keys.NOTE_BLOCK_SOUND).map {
        it.toAPI()
    }.getOrNull()
}, {
    offer(Keys.NOTE_BLOCK_SOUND, ResourceKey.resolve(it.toString()))
}, {
    remove(Keys.NOTE_BLOCK_SOUND)
})

internal val baseColorDataConverter = SpongeItemStackDataComponentConverter<DyeColor>({
    val key = get(Keys.DYE_COLOR).flatMap { DyeColors.registry().findValueKey(it) }.getOrNull()?.toAPI()
    key?.let { DyeColor.findByKey(it) }
}, {
    DyeColors.registry().findValue<org.spongepowered.api.data.type.DyeColor>(ResourceKey.resolve(it.key.toString())).ifPresent { dyeColor ->
        offer(Keys.DYE_COLOR, dyeColor)
    }
}, {
    remove(Keys.DYE_COLOR)
})

internal val recipesDataConverter = SpongeItemStackDataComponentConverter<List<Key>>({
    // TODO: Cannot find a data key for this in sponges API ?!
    null
}, { })

