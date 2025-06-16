package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.adventure.toAPI
import com.wolfyscript.scafall.wrappers.world.Color
import com.wolfyscript.scafall.wrappers.world.items.data.CustomModelData
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.data.Keys
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

val displayNameConverter = SpongeItemStackDataComponentConverter({ get(Keys.CUSTOM_NAME).getOrNull() }, {
    offer(Keys.CUSTOM_NAME, it)
}, {
    remove(Keys.CUSTOM_NAME)
})

val displayLoreConverter = SpongeItemStackDataComponentConverter(
    { ItemLore(get(Keys.LORE).getOrElse { emptyList() }) },
    { offer(Keys.LORE, it.lines) })

val itemNameConverter = SpongeItemStackDataComponentConverter(
    {
        get(Keys.ITEM_NAME).getOrNull()
    }, {
        offer(Keys.ITEM_NAME, it)
    }, {
        remove(Keys.ITEM_NAME)
    }
)

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

val itemModelConverter = SpongeItemStackDataComponentConverter({ get(Keys.MODEL).map { it.toAPI() }.getOrNull() }, {
    offer(
        Keys.MODEL,
        ResourceKey.of(it.namespace, it.value)
    )
}, {
    remove(Keys.MODEL)
})
