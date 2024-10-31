package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.ItemStackDataKeyConverter
import com.wolfyscript.scafall.wrappers.world.items.data.DyedColor
import org.spongepowered.api.data.Keys
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.util.Color
import kotlin.jvm.optionals.getOrNull

val dyedColorDataConverter = ItemStackDataKeyConverter({
    if (type() == ItemTypes.LEATHER_HELMET || type() == ItemTypes.LEATHER_CHESTPLATE || type() == ItemTypes.LEATHER_BOOTS || type() == ItemTypes.LEATHER_LEGGINGS) {
        return@ItemStackDataKeyConverter get(Keys.COLOR).map {
            DyedColor(true, it.rgb())
        }.getOrNull()
    }
    null
}, {
    if (type() == ItemTypes.LEATHER_HELMET || type() == ItemTypes.LEATHER_CHESTPLATE || type() == ItemTypes.LEATHER_BOOTS || type() == ItemTypes.LEATHER_LEGGINGS) {
        offer(Keys.COLOR, Color.ofRgb(it.rgb))
    }
})