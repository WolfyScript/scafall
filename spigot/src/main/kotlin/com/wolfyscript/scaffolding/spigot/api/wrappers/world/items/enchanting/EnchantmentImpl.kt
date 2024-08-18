package com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.enchanting

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.identifiers.api
import org.bukkit.enchantments.Enchantment

internal class EnchantmentImpl(val bukkit: Enchantment) : com.wolfyscript.scaffolding.wrappers.world.items.enchanting.Enchantment {

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
