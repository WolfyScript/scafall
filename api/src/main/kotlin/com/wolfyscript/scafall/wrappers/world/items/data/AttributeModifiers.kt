package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.attribute.Attribute
import com.wolfyscript.scafall.wrappers.world.attribute.AttributeModifier

data class AttributeModifiers(
    val modifiers: List<Entry>,
) {

    data class Entry(
        val attribute: Attribute,
        val modifier: AttributeModifier
    )

}
