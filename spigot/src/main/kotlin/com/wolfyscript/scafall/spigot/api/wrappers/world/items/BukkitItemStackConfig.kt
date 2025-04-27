package com.wolfyscript.scafall.spigot.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackConfigCommon
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.*
import com.wolfyscript.scafall.nbt.*
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTList
import de.tr7zw.nbtapi.NBTType
import de.tr7zw.nbtapi.iface.ReadableNBT
import de.tr7zw.nbtapi.iface.ReadableNBTList
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.function.BiFunction

class BukkitItemStackConfig @JsonCreator constructor(@JsonProperty("stack") stack: ItemStackSnapshot) :
    ItemStackConfigCommon(stack) {

    override fun constructItemStack(
        context: EvalContext,
        miniMessage: MiniMessage?,
        tagResolvers: TagResolver
    ): ItemStack {
        return stack.createStack().apply {
            overrides.forEach { (key, value) ->
                value.applyTo(this, context, miniMessage, tagResolvers)
            }
        }
    }

}
