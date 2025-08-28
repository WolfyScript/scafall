package com.wolfyscript.scafall.spigot.compat.itemsadder

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import dev.lone.itemsadder.api.CustomStack

class ItemsAdderStackIdentifierParser(override val priority: Int = 1500) : ItemStackIdentifier.Parser<ItemsAdderStackIdentifier> {

    override fun from(stack: ItemStackLike<*, *>): ItemsAdderStackIdentifier? {
        val IAStack = CustomStack.byItemStack(stack.unwrapSpigot()) ?: return null
        return ItemsAdderStackIdentifier(IAStack.namespacedID)
    }
}