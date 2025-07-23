package com.wolfyscript.scafall.spigot.compat.itemsadder

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import dev.lone.itemsadder.api.CustomStack

class ItemsAdderStackIdentifierParser : ItemStackIdentifier.Parser<ItemsAdderStackIdentifier> {

    override val priority: Int = 1500

    override fun from(stack: ItemStack): ItemsAdderStackIdentifier? {
        val IAStack = CustomStack.byItemStack(stack.unwrapSpigot()) ?: return null
        return ItemsAdderStackIdentifier(IAStack.namespacedID)
    }
}