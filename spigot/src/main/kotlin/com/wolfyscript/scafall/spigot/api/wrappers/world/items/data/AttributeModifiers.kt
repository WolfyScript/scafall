package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.common.api.wrappers.world.attribute.CommonAttributeModifier
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierOperation
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierSlot
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import java.util.*

fun AttributeModifier.wrap(): com.wolfyscript.scafall.wrappers.world.attribute.AttributeModifier {
    return CommonAttributeModifier(
        slot = ModifierSlot.valueOf(slotGroup.toString().uppercase(Locale.ROOT)),
        id = key.api(),
        amount = amount,
        operation = when (operation) {
            AttributeModifier.Operation.ADD_NUMBER -> ModifierOperation.ADD_VALUE
            AttributeModifier.Operation.ADD_SCALAR -> ModifierOperation.ADD_MULTIPLIED_BASE
            AttributeModifier.Operation.MULTIPLY_SCALAR_1 -> ModifierOperation.ADD_MULTIPLIED_TOTAL
        },
    )
}

fun com.wolfyscript.scafall.wrappers.world.attribute.AttributeModifier.unwrap() : AttributeModifier {
    return AttributeModifier(
        id.bukkit(),
        amount,
        AttributeModifier.Operation.valueOf(
            operation.toString().uppercase(Locale.getDefault())
        ),
        EquipmentSlotGroup.getByName(slot.id) ?: EquipmentSlotGroup.ANY,
    )
}