package com.wolfyscript.scafall.common.api.wrappers.world.attribute

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.attribute.Attribute
import com.wolfyscript.scafall.wrappers.world.attribute.AttributeModifier
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierOperation
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierSlot

class CommonAttributeModifier(
    override val slot: ModifierSlot,
    override val id: Key,
    override val amount: Double,
    override val operation: ModifierOperation,
) : AttributeModifier