package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.Color

data class CustomModelData(
    val floats: List<Float>,
    val flags: List<Boolean>,
    val strings: List<String>,
    val colors: List<Color>
)
