package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.data.ItemStackDataKeyConverter
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
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
val unbreakableDataConverter = ItemStackDataKeyConverter({
    if (get(Keys.IS_UNBREAKABLE).getOrNull() == true) {
        return@ItemStackDataKeyConverter Unbreakable(get(Keys.HIDE_UNBREAKABLE).getOrNull() ?: false)
    }
    null
}, {
    offer(Keys.IS_UNBREAKABLE, true)
    offer(Keys.HIDE_UNBREAKABLE, it.showInTooltip)
})
val damageConverter = ItemStackDataKeyConverter({
    get(Keys.ITEM_DURABILITY).map {
        get(Keys.MAX_DURABILITY).getOrElse { 0 } - it
    }.getOrNull()
}, { damage ->
    get(Keys.MAX_DURABILITY).ifPresent { max ->
        offer(Keys.ITEM_DURABILITY, max - damage)
    }
})
val repairCostConverter = ItemStackDataKeyConverter({
    get(Keys.REPAIR_COST).getOrNull()
}, {
    offer(Keys.REPAIR_COST, it)
})

// item display options
val displayNameConverter = ItemStackDataKeyConverter({ get(Keys.CUSTOM_NAME).getOrNull() }, {
    offer(Keys.CUSTOM_NAME, it)
})
val displayLoreConverter = ItemStackDataKeyConverter({ ItemLore(get(Keys.LORE).getOrElse { emptyList() }) }, { offer(Keys.LORE, it.lines) })
val customModelDataConverter = ItemStackDataKeyConverter({
    get(Keys.CUSTOM_MODEL_DATA).getOrNull()
}, {
    offer(Keys.CUSTOM_MODEL_DATA, it)
})

// Note blocks
val instrumentConverter = ItemStackDataKeyConverter<Key>({
    get(Keys.INSTRUMENT_TYPE).map {
        InstrumentTypes.registry().valueKey(it).toAPI()
    }.getOrNull()
}, {
    InstrumentTypes.registry().findValue<InstrumentType>(ResourceKey.resolve(it.toString())).ifPresent { instrument ->
        offer(Keys.INSTRUMENT_TYPE, instrument)
    }
})
val noteBlockSoundConverter = ItemStackDataKeyConverter({
    get(Keys.NOTE_BLOCK_SOUND).map {
        it.toAPI()
    }.getOrNull()
}, {
    offer(Keys.NOTE_BLOCK_SOUND, ResourceKey.resolve(it.toString()))
})

internal val baseColorDataConverter = ItemStackDataKeyConverter<DyeColor>({
    val key = get(Keys.DYE_COLOR).flatMap { DyeColors.registry().findValueKey(it) }.getOrNull()?.toAPI()
    key?.let { DyeColor.findByKey(it) }
}, {
    DyeColors.registry().findValue<org.spongepowered.api.data.type.DyeColor>(ResourceKey.resolve(it.key.toString())).ifPresent { dyeColor ->
        offer(Keys.DYE_COLOR, dyeColor)
    }
})

internal val recipesDataConverter = ItemStackDataKeyConverter<List<Key>>({
    // TODO: Cannot find a data key for this in sponges API ?!
    null
}, { })

