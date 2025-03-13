package com.wolfyscript.scafall.wrappers.world.attribute

import com.wolfyscript.scafall.identifier.Key

interface AttributeModifier {
    val slot: ModifierSlot
    val id: Key
    val amount: Double
    val operation: ModifierOperation
}