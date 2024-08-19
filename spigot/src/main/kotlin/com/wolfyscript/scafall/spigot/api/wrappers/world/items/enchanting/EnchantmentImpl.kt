package com.wolfyscript.scafall.spigot.api.wrappers.world.items.enchanting

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.identifiers.api
import org.bukkit.enchantments.Enchantment

internal class EnchantmentImpl(val bukkit: Enchantment) : com.wolfyscript.scafall.wrappers.world.items.enchanting.Enchantment {

    override fun maxLevel(): Int {
        return bukkit.maxLevel
    }

    override fun minLevel(): Int {
        return bukkit.startLevel
    }

    override fun key(): Key {
        return bukkit.key.api()
    }
}
