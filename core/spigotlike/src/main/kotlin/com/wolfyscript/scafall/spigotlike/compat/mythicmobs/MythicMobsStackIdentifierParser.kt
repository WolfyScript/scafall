package com.wolfyscript.scafall.spigotlike.compat.mythicmobs

import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import io.lumine.mythic.bukkit.MythicBukkit

class MythicMobsStackIdentifierParser(override val priority: Int = 1600) : ItemStackIdentifier.Parser<MythicMobsStackIdentifier> {

    override fun from(stack: ItemStackLike): MythicMobsStackIdentifier? {
        val type = MythicBukkit.inst().itemManager.getMythicTypeFromItem(stack.unwrapSpigot()) ?: return null
        return MythicMobsStackIdentifier(type)
    }
}