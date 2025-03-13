package com.wolfyscript.scafall.wrappers.world.items.data

data class Food(
    val nutrition: Int,
    val saturation: Float,
    val canAlwaysEat: Boolean = false,
)