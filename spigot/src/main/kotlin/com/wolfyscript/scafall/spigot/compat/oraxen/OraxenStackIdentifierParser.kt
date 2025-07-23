package com.wolfyscript.scafall.spigot.compat.oraxen

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import io.th0rgal.oraxen.api.OraxenItems

class OraxenStackIdentifierParser : ItemStackIdentifier.Parser<OraxenItemStackIdentifier> {

    override val priority: Int = 0

    override fun from(stack: ItemStack): OraxenItemStackIdentifier? {
        val id = OraxenItems.getIdByItem(stack.unwrapSpigot()) ?: return null
        return OraxenItemStackIdentifier(id)
    }
}