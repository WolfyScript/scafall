package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.common.api.wrappers.world.attribute.CommonAttribute
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.wrap
import com.wolfyscript.scafall.wrappers.world.items.data.AttributeModifiers
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import org.bukkit.Registry

internal val attributeModifiersPaperConverter = PaperDataAPIConverter<AttributeModifiers>(
    {
        val stack = unwrap()
        if (stack.hasData(DataComponentTypes.ATTRIBUTE_MODIFIERS)) {
            val attributeModifiers = stack.getData(DataComponentTypes.ATTRIBUTE_MODIFIERS)!!
            val converted = AttributeModifiers(
                attributeModifiers.modifiers().map { AttributeModifiers.Entry(CommonAttribute(it.attribute().key.api()), it.modifier().wrap()) }
            )
            return@PaperDataAPIConverter Result.success(converted)
        }
        return@PaperDataAPIConverter Result.success(null)
    },
    {
        val stack = unwrap()
        val builder = ItemAttributeModifiers.itemAttributes()
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
