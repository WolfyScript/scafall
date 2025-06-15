package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import net.kyori.adventure.text.Component

data class ItemLore(
    val lines: List<Component>
) {

}

data class ItemLoreConfig(
    val lines: List<ValueProvider<String>>
)
