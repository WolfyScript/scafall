package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.AttributeModifiers
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.data.Keys
import org.spongepowered.api.entity.attribute.AttributeModifier
import org.spongepowered.api.entity.attribute.AttributeOperation
import org.spongepowered.api.entity.attribute.AttributeOperations
import org.spongepowered.api.entity.attribute.type.AttributeType
import org.spongepowered.api.entity.attribute.type.AttributeTypes
import org.spongepowered.api.item.inventory.equipment.EquipmentType
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes

val attributeModifiersDataConverter = SpongeItemStackDataComponentConverter<AttributeModifiers>({
    val modifiers = buildList {
        for (attributeType in AttributeTypes.registry().streamEntries()) {
            for (equipmentType in EquipmentTypes.registry().stream()) {
                val modifiers = attributeModifiers(attributeType.value(), equipmentType)
                for (modifier in modifiers) {
                    add(
                        AttributeModifiers.Modifier(
                            type = attributeType.key().toAPI(),
                            slot = AttributeModifiers.Modifier.Slot.valueOf(equipmentType.group().toString()),
                            amount = modifier.amount(),
                            id = modifier.key().toAPI(),
                            operation = AttributeModifiers.Modifier.Operation.valueOf(
                                modifier.operation().toString().uppercase()
                            )
                        )
                    )
                }
            }
        }
    }

    AttributeModifiers(modifiers, get(Keys.HIDE_ATTRIBUTES).orElse(false))
}, { attributeModifiers ->
    for (modifier in attributeModifiers.modifiers) {
        AttributeTypes.registry()
            .findValue<AttributeType>(ResourceKey.resolve(modifier.type.toString()))
            .ifPresent { attributeType ->
                AttributeOperations.registry()
                    .findValue<AttributeOperation>(ResourceKey.minecraft(modifier.operation.id))
                    .ifPresent { attributeOperation ->
                        addAttributeModifier(
                            attributeType,
                            AttributeModifier.builder()
                                .operation(attributeOperation)
                                .amount(modifier.amount)
                                .key(ResourceKey.resolve(modifier.id.toString()))
                                .build(),
                            EquipmentTypes.registry()
                                .findValue<EquipmentType>(ResourceKey.minecraft(modifier.id.toString()))
                                .orElse(EquipmentTypes.BODY.get()) // Cannot find the ANY type, fallback to the BODY type instead
                        )
                    }
            }
    }
})