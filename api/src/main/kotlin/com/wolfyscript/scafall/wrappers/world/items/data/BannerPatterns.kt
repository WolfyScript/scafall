package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.DyeColor

data class BannerPatterns(
    val layers: List<Layer>
) {

    data class Layer(
        val shape: Key,
        val color: DyeColor
    )

}
