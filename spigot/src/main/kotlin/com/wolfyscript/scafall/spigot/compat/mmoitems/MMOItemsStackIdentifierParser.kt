package com.wolfyscript.scafall.spigot.compat.mmoitems

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.compat.mythicmobs.MythicMobsStackIdentifier
import com.wolfyscript.scafall.spigot.compat.oraxen.OraxenItemStackIdentifier
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import io.lumine.mythic.bukkit.MythicBukkit
import io.th0rgal.oraxen.api.OraxenItems
import net.Indyuce.mmoitems.MMOItems

class MMOItemsStackIdentifierParser(override val priority: Int = 1600) : ItemStackIdentifier.Parser<MMOItemsStackIdentifier> {

    override fun from(stack: ItemStack): MMOItemsStackIdentifier? {
        val type = MMOItems.getType(stack.unwrapSpigot()) ?: return null
        val item = MMOItems.getID(stack.unwrapSpigot()) ?: return null
        return MMOItemsStackIdentifier(type, item)
    }
}