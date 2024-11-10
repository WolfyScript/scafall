package com.wolfyscript.scafall.sponge.api.wrappers.world.items

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class SpongeItemStackConfig(
    stack: com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot,
) : ItemStackConfig(stack) {

    override fun constructItemStack(
        context: EvalContext,
        miniMessage: MiniMessage?,
        tagResolvers: TagResolver
    ): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        val wrappedStack = stack.createStack()
        return wrappedStack
    }

}