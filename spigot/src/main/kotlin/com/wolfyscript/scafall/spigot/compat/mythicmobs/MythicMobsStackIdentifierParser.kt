package com.wolfyscript.scafall.spigot.compat.mythicmobs

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.compat.oraxen.OraxenItemStackIdentifier
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import io.lumine.mythic.bukkit.MythicBukkit
import io.th0rgal.oraxen.api.OraxenItems

class MythicMobsStackIdentifierParser(override val priority: Int = 1600) : ItemStackIdentifier.Parser<MythicMobsStackIdentifier> {

    override fun from(stack: ItemStack): MythicMobsStackIdentifier? {
        val type = MythicBukkit.inst().itemManager.getMythicTypeFromItem(stack.unwrapSpigot()) ?: return null
        return MythicMobsStackIdentifier(type)
    }
}