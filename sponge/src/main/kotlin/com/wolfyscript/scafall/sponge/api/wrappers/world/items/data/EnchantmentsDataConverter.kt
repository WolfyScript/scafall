package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.EnchantmentsImpl
import com.wolfyscript.scafall.sponge.api.data.ItemStackDataKeyConverter
import com.wolfyscript.scafall.sponge.api.wrappers.world.items.enchanting.EnchantmentImpl
import com.wolfyscript.scafall.wrappers.world.items.data.Enchantments
import com.wolfyscript.scafall.wrappers.world.items.enchanting.Enchantment
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.data.Keys
import org.spongepowered.api.item.enchantment.EnchantmentType
import org.spongepowered.api.item.enchantment.EnchantmentTypes
import kotlin.jvm.optionals.getOrNull

internal val enchantmentsDataConverter = ItemStackDataKeyConverter<Enchantments>({
    EnchantmentsImpl(
        get(Keys.HIDE_ENCHANTMENTS).orElse(false),
        get(Keys.APPLIED_ENCHANTMENTS).map {
            it.associateTo(mutableMapOf<Enchantment, Int>()) { enchantment ->
                Pair(EnchantmentImpl(enchantment), enchantment.level())
            }
        }.orElse(mutableMapOf())
    )
}, { enchantments ->
    enchantments as EnchantmentsImpl
    offer(Keys.HIDE_ENCHANTMENTS, enchantments.showInTooltip)
    offer(Keys.APPLIED_ENCHANTMENTS, enchantments.enchants.map {
        val type = EnchantmentTypes.registry().findValue<EnchantmentType>(ResourceKey.resolve(it.key.key().toString())).getOrNull()
        org.spongepowered.api.item.enchantment.Enchantment.of(type, it.value)
    })
})

internal val enchantmentOverrideDataConverter = ItemStackDataKeyConverter<Boolean>({ TODO("Not implemented yet!") }, { TODO("Not implemented yet!") })