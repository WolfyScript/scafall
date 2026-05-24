package com.wolfyscript.scafall.spigotlike.compat.oraxen

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import io.th0rgal.oraxen.api.OraxenItems

class OraxenStackIdentifierParser(override val priority: Int = 1900) : ItemStackIdentifier.Parser<OraxenItemStackIdentifier> {

    override fun from(stack: ItemStackLike): OraxenItemStackIdentifier? {
        val id = OraxenItems.getIdByItem(stack.unwrapSpigot()) ?: return null
        return OraxenItemStackIdentifier(id)
    }
}