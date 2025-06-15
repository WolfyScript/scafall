package com.wolfyscript.scafall.wrappers.world.items.enchanting

import com.wolfyscript.scafall.identifier.Key

interface Enchantment {

    fun maxLevel(): Int

    fun minLevel(): Int

    fun key(): Key

}
