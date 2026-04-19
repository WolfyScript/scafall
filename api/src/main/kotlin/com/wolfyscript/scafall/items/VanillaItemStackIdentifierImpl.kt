package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.wrappers.unwrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.wrap
import net.minecraft.world.item.ItemStack

class VanillaItemStackIdentifierImpl : VanillaItemStackIdentifier {

    override val stack: ScafallItemStack

    constructor(stack: ScafallItemStack) {
        this.stack = stack
    }

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        val other = stack.unwrap()
        val thisStack = this.stack.unwrap()
        if (thisStack == other) {
            return true // Same instance of stacks, so they must be equal!
        }
        if (matchTags) {
            return ItemStack.isSameItemSameComponents(thisStack, other)
        }
        return ItemStack.isSameItem(thisStack, other)
    }

    override fun create(): ScafallItemStack {
        return stack.unwrap().copy().wrap()
    }

    override fun toString(): String {
        return "vanilla($stack)"
    }

    class Parser : VanillaItemStackIdentifier.Parser {

        override val priority: Int = 0

        override fun from(stack: ItemStackLike): VanillaItemStackIdentifierImpl {
            return VanillaItemStackIdentifierImpl(stack.unwrap().wrap())
        }

    }

}