package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toBukkit
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toWrapper
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.BannerPatterns
import org.bukkit.Registry
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.meta.BannerMeta

internal val bannerPatternsItemMetaConverter = ItemMetaDataKeyConverter<BannerPatterns>(
    {
        if (this is BannerMeta) {
            BannerPatterns(
                patterns.map {
                    BannerPatterns.Layer(it.pattern.key.toAPI(), it.color.toWrapper())
                }
            )
        } else BannerPatterns(emptyList())
    },
    { bannerPatterns ->
        if (this is BannerMeta) {
            patterns = bannerPatterns.layers.map {
                Pattern(it.color.toBukkit(), Registry.BANNER_PATTERN.get(it.shape.bukkit()) ?: PatternType.BASE)
            }
        }
    })
