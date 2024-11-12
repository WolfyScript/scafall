package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.AttributeModifiers
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import java.util.*

internal val attributeModifiersItemMetaConverter = ItemMetaDataKeyConverter(
    {
        val modifiers = buildList {
            attributeModifiers?.forEach { type, modifier ->
                val wrappedModifier = AttributeModifiers.Modifier(
                    type = type.key.toAPI(),
                    slot = AttributeModifiers.Modifier.Slot.ANY,
                    id = modifier.key.toAPI(),
                    amount = modifier.amount,
                    operation = AttributeModifiers.Modifier.Operation.valueOf(modifier.operation.toString().uppercase(Locale.getDefault())),
                )
                add(wrappedModifier)
            }
        }

        AttributeModifiers(modifiers, hasItemFlag(ItemFlag.HIDE_ATTRIBUTES))
    },
    { attributeModifiers ->
        for (modifier in attributeModifiers.modifiers) {
            Registry.ATTRIBUTE[modifier.type.bukkit()]?.let {
                addAttributeModifier(
                    it,
                    AttributeModifier(
                        modifier.id.bukkit(),
                        modifier.amount,
                        AttributeModifier.Operation.valueOf(modifier.operation.toString().uppercase(Locale.getDefault())),
                        EquipmentSlotGroup.getByName(modifier.slot.id) ?: EquipmentSlotGroup.ANY,
                    )
                )
            }
        }
    }
)
