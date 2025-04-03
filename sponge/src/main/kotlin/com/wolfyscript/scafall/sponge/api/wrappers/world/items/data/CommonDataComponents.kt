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
    DyeColors.registry().findValue<org.spongepowered.api.data.type.DyeColor>(ResourceKey.resolve(it.key.toString()))
        .ifPresent { dyeColor ->
            offer(Keys.DYE_COLOR, dyeColor)
        }
}, {
    remove(Keys.DYE_COLOR)
})

internal val recipesDataConverter = SpongeItemStackDataComponentConverter<List<Key>>({
    // TODO: Cannot find a data key for this in sponges API ?!
    null
}, { })

