package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.ItemStackDataKeyConverter
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.data.BannerPatterns
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.data.Keys
import org.spongepowered.api.data.meta.BannerPatternLayer
import org.spongepowered.api.data.type.BannerPatternShape
import org.spongepowered.api.data.type.BannerPatternShapes
import org.spongepowered.api.data.type.DyeColors
import kotlin.jvm.optionals.getOrNull

val bannerPatternDataConverter = ItemStackDataKeyConverter<BannerPatterns>({
    get(Keys.BANNER_PATTERN_LAYERS).map { patterns ->
        BannerPatterns(patterns.mapNotNull {
            val shapeKey = BannerPatternShapes.registry().findValueKey(it.shape()).getOrNull()
            val color = DyeColors.registry().findValueKey(it.color()).map { color -> DyeColor.findByKey(color.toAPI()) }.orElse(null)
            if (shapeKey != null && color != null) {
                return@map BannerPatterns.Layer(shapeKey.toAPI(), color)
            }
            null
        })
    }
    null
},{
    offer(Keys.BANNER_PATTERN_LAYERS, it.layers.map { layer ->
        val shape = BannerPatternShapes.registry().findValue<BannerPatternShape>(ResourceKey.resolve(layer.shape.toString())).getOrNull()
        val color = DyeColors.registry().findValue<org.spongepowered.api.data.type.DyeColor>(ResourceKey.resolve(layer.color.key.toString())).getOrNull()

        if (shape != null && color != null) {
            BannerPatternLayer.of(shape, color)
        } else null
    })
})