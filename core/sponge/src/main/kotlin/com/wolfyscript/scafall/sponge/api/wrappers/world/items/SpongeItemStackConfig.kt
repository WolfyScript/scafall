package com.wolfyscript.scafall.sponge.api.wrappers.world.items

import com.wolfyscript.scafall.items.ItemStackConfigCommon
import com.wolfyscript.scafall.eval.context.EvalContext
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class SpongeItemStackConfig(
    stack: com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot,
) : ItemStackConfigCommon(stack) {

    override fun constructItemStack(
        context: EvalContext,
        miniMessage: MiniMessage?,
        tagResolvers: TagResolver
    ): com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack {
        return stack.createStack().apply {
            overrides.forEach { (key, value) ->
                value.applyTo(this, context, miniMessage, tagResolvers)
            }
        }
    }

}