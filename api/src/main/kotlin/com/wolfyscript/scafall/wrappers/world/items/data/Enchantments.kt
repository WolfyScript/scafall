package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.items.enchanting.Enchantment

interface Enchantments {

    fun level(enchantment: Enchantment): Int?

    fun set(enchantment: Enchantment, level: Int)

}
