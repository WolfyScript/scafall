package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toBukkit
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toWrapper
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.BannerPatterns
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BannerPatternLayers
import org.bukkit.Registry
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.inventory.meta.BannerMeta

internal val bannerPatternsPaperConverter = PaperDataAPIConverter<BannerPatterns>(
    {
        val bannerPatterns = unwrap().getData(DataComponentTypes.BANNER_PATTERNS)
        if (bannerPatterns != null) {
            return@PaperDataAPIConverter Result.success(
                BannerPatterns(
                    bannerPatterns.patterns().map {
                        BannerPatterns.Layer(it.pattern.key.api(), it.color.toWrapper())
                    }
                )
            )
        }
        return@PaperDataAPIConverter Result.success(null)
    },
    { bannerPatterns ->
        val stack = unwrap()
        stack.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers(
            bannerPatterns.layers.map {
                Pattern(it.color.toBukkit(), Registry.BANNER_PATTERN.get(it.shape.bukkit()) ?: PatternType.BASE)
            }
        ))
        return@PaperDataAPIConverter Result.success(this)
    },
    {
        unwrap().unsetData(DataComponentTypes.BANNER_PATTERNS)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

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
            patterns = bannerPatterns?.layers?.map {
                Pattern(it.color.toBukkit(), Registry.BANNER_PATTERN.get(it.shape.bukkit()) ?: PatternType.BASE)
            } ?: emptyList()
        }
    })
