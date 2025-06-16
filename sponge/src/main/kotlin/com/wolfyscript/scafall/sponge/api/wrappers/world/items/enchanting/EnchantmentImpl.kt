package com.wolfyscript.scafall.sponge.api.wrappers.world.items.enchanting

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.adventure.toAPI
import com.wolfyscript.scafall.wrappers.world.items.enchanting.Enchantment
import org.spongepowered.api.item.enchantment.EnchantmentTypes

class EnchantmentImpl(val sponge: org.spongepowered.api.item.enchantment.Enchantment) : Enchantment {

    override fun maxLevel(): Int = sponge.type().maximumLevel()

    override fun minLevel(): Int = sponge.type().minimumLevel()

    override fun key(): Key = EnchantmentTypes.registry().findValueKey(sponge.type()).get().toAPI() // This will only fail if the enchantment type is not in the registry
}