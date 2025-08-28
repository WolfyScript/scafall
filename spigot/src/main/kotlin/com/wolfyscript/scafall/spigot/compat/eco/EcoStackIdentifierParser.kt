package com.wolfyscript.scafall.spigot.compat.eco

import com.willfp.eco.core.items.Items
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

class EcoStackIdentifierParser(override val priority: Int = 100) : ItemStackIdentifier.Parser<EcoStackIdentifier> {

    override fun from(stack: ItemStackLike<*, *>): EcoStackIdentifier? {
        val item = Items.getCustomItem(stack.unwrapSpigot()) ?: return null
        return EcoStackIdentifier(item.key.api())
    }
}