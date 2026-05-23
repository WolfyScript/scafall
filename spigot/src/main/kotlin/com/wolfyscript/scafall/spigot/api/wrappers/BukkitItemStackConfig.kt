package com.wolfyscript.scafall.spigot.api.wrappers

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfigCommon
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

@Deprecated("Still work in progress, not ready for use")
class BukkitItemStackConfig @JsonCreator constructor(@JsonProperty("stack") stack: ItemStackSnapshot) :
    ItemStackConfigCommon(stack) {

    override fun constructItemStack(
        context: EvalContext,
        miniMessage: MiniMessage?,
        tagResolvers: TagResolver
    ): ScafallItemStack {
        return stack.create().apply {
            overrides.forEach { (key, value) ->
                value.applyTo(this, context, miniMessage, tagResolvers)
            }
        }
    }

}