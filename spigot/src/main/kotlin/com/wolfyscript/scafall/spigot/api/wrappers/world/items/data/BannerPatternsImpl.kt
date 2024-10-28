package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toWrapper
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.data.BannerPatterns
import org.bukkit.block.banner.Pattern
import org.bukkit.inventory.meta.BannerMeta
import java.util.*

internal val bannerPatternsItemMetaConverter = ItemMetaDataKeyConverter<BannerPatterns>(
    {
        if (this is BannerMeta) BannerPatternsImpl(patterns) else BannerPatternsImpl(emptyList())
    },
    { bannerPatterns ->
        if (this is BannerMeta && bannerPatterns is BannerPatternsImpl) {
            patterns = bannerPatterns.layers.map { it.toBukkit() }
        }
    })

class BannerPatternsImpl(layers: List<Pattern>) : BannerPatterns {

    private val layerWrappers: List<Layer> = layers.map { Layer(it) }

    override val layers: List<Layer> = layerWrappers

    class Layer(private val pattern: Pattern) : BannerPatterns.Layer {

        fun toBukkit(): Pattern {
            return pattern
        }

        override val shape: Key = Key.Companion.key(Key.MINECRAFT_NAMESPACE, pattern.pattern.name.lowercase(Locale.getDefault()))
        override val color: DyeColor = pattern.color.toWrapper()
    }

}