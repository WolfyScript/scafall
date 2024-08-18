package com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.data

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.toWrapper
import com.wolfyscript.scaffolding.wrappers.world.items.DyeColor
import com.wolfyscript.scaffolding.wrappers.world.items.data.BannerPatterns
import org.bukkit.block.banner.Pattern
import org.bukkit.inventory.meta.BannerMeta
import java.util.*

class BannerPatternsImpl(layers: List<Pattern>) : BannerPatterns {

    companion object {
        internal val ITEM_META_CONVERTER = ItemMetaDataKeyConverter<BannerPatterns>(
            {
                if (this is BannerMeta) BannerPatternsImpl(patterns) else BannerPatternsImpl(emptyList())
            },
            { bannerPatterns ->
                if (this is BannerMeta && bannerPatterns is BannerPatternsImpl) {
                    patterns = bannerPatterns.layers().map { it.toBukkit() }
                }
            })
    }

    private val layerWrappers: List<Layer> = layers.map { Layer(it) }

    override fun layers(): List<Layer> {
        return layerWrappers
    }

    class Layer(private val pattern: Pattern) : BannerPatterns.Layer {

        fun toBukkit(): Pattern {
            return pattern
        }

        override fun shape(): Key {
            return Key.Companion.key(Key.MINECRAFT_NAMESPACE, pattern.pattern.name.lowercase(Locale.getDefault()))
        }

        override fun color(): DyeColor {
            return pattern.color.toWrapper()
        }

    }

}