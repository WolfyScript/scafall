package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.items.data.Enchantments
import com.wolfyscript.scafall.wrappers.world.items.enchanting.Enchantment

class EnchantmentsImpl(val enchants: MutableMap<Enchantment, Int>) : Enchantments {

    override fun level(enchantment: Enchantment): Int? {
        return enchants[enchantment]
    }

    override fun set(enchantment: Enchantment, level: Int) {
        enchants[enchantment] = level
    }

}
