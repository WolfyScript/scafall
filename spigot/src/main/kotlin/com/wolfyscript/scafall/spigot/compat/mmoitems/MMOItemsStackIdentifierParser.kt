package com.wolfyscript.scafall.spigot.compat.mmoitems

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import net.Indyuce.mmoitems.MMOItems

class MMOItemsStackIdentifierParser(override val priority: Int = 1600) : ItemStackIdentifier.Parser<MMOItemsStackIdentifier> {

    override fun from(stack: ItemStackLike<*, *>): MMOItemsStackIdentifier? {
        val type = MMOItems.getType(stack.unwrapSpigot()) ?: return null
        val item = MMOItems.getID(stack.unwrapSpigot()) ?: return null
        return MMOItemsStackIdentifier(type, item)
    }
}