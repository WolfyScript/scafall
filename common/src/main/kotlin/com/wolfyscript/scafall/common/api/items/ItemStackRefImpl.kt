package com.wolfyscript.scafall.common.api.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.items.ItemStackRef
import com.wolfyscript.scafall.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

/**
 * Constructs a new instance of a [ItemStackRef].
 */
class ItemStackRefImpl(
    override val amount: Int,
    override val identifier: ItemStackIdentifier,
) : ItemStackRef {

    @get:JsonIgnore
    override val originalStack: ItemStack? by lazy { identifier.create() }

    override fun swapParser(parser: ItemStackIdentifier.Parser<*>): Result<ItemStackRef> {
        if (originalStack == null) {
            return Result.failure(IllegalStateException("ItemStackRef has no ItemStack to parse!"))
        }
        val parsed = parser.from(originalStack!!)
        if (parsed == null) {
            return Result.failure(IllegalStateException("Failed to swap to parser $parser! Emtpy parse result!"))
        }
        return Result.success(ItemStackRefImpl(amount, parsed))
    }

    override fun matches(
        stack: ItemStack,
        matchTags: Boolean,
    ): Boolean {
        return  identifier.matches(stack, matchTags)
    }

    override fun create(): ItemStack {
        val stack = identifier.create().unwrap()
        stack.count = amount
        return stack.wrap()
    }

    override fun toString(): String {
        return "ItemStackRefImpl(amount=$amount, identifier=$identifier)"
    }

}