package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.google.common.collect.HashMultimap
import com.wolfyscript.scafall.common.api.wrappers.world.attribute.CommonAttribute
import com.wolfyscript.scafall.common.api.wrappers.world.attribute.CommonAttributeModifier
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierOperation
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierSlot
import com.wolfyscript.scafall.wrappers.world.items.data.AttributeModifiers
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import java.util.*

internal val attributeModifiersItemMetaConverter = ItemMetaDataKeyConverter(
    {
        val modifiers = buildList {
            attributeModifiers?.forEach { type, modifier ->
                add(AttributeModifiers.Entry(CommonAttribute(type.key.api()), modifier.wrap()))
            }
        }
        AttributeModifiers(modifiers)
    },
    { attributeModifiers ->
        // clear existing modifiers
        this.attributeModifiers = HashMultimap.create()
        if (attributeModifiers != null) {
            for (entry in attributeModifiers.modifiers) {
                Registry.ATTRIBUTE[entry.attribute.key().bukkit()]?.let {
                    addAttributeModifier(
                        it,
                        entry.modifier.unwrap()
                    )
                }
            }
        }
    }
)

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