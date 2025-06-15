package com.wolfyscript.scafall.wrappers.world.items.data

import net.kyori.adventure.text.Component

interface WrittenBookContent {
    fun pages(): List<Component>

    fun title(): Component

    fun author(): String
}
