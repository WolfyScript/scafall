package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.google.common.collect.HashMultimap
import com.wolfyscript.scafall.common.api.wrappers.world.attribute.CommonAttribute
import com.wolfyscript.scafall.common.api.wrappers.world.attribute.CommonAttributeModifier
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierOperation
import com.wolfyscript.scafall.wrappers.world.attribute.ModifierSlot
import com.wolfyscript.scafall.wrappers.world.items.data.AttributeModifiers
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import java.util.*

internal val attributeModifiersPaperConverter = PaperDataAPIConverter<AttributeModifiers>(
    {
        val stack = unwrap()
        if (stack.hasData(DataComponentTypes.ATTRIBUTE_MODIFIERS)) {
            val attributeModifiers = stack.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)!!
            val converted = AttributeModifiers(
                attributeModifiers.modifiers().map { AttributeModifiers.Entry(CommonAttribute(it.attribute().key.api()), it.modifier().wrap()) },
                attributeModifiers.showInTooltip()
            )
            return@PaperDataAPIConverter Result.success(converted)
        }
        return@PaperDataAPIConverter Result.success(null)
    },
    {
        val stack = unwrap()
        val builder = ItemAttributeModifiers.itemAttributes().showInTooltip(it!!.showInTooltip)
        for (entry in it.modifiers) {
            builder.addModifier(Registry.ATTRIBUTE.get(entry.attribute.key().bukkit()), entry.modifier.unwrap())
        }
        stack.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, builder.build())
        return@PaperDataAPIConverter Result.success(this)
    },
    {
        unwrap().unsetData(DataComponentTypes.ATTRIBUTE_MODIFIERS)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

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