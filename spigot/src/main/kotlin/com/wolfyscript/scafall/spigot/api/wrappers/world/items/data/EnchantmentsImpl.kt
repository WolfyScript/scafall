package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.EnchantmentsImpl
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.enchanting.EnchantmentImpl
import com.wolfyscript.scafall.wrappers.world.items.data.Enchantable
import com.wolfyscript.scafall.wrappers.world.items.data.Enchantments
import com.wolfyscript.scafall.wrappers.world.items.enchanting.Enchantment
import org.bukkit.inventory.ItemFlag

internal val enchantmentsItemMetaConverter = ItemMetaDataKeyConverter<Enchantments>(
    {
        EnchantmentsImpl(
            !hasItemFlag(ItemFlag.HIDE_ENCHANTS),
            enchants.mapKeys<org.bukkit.enchantments.Enchantment, Int, Enchantment> {
                EnchantmentImpl(it.key)
            }.toMutableMap()
        )
    },
    {
        removeEnchantments()
        if (it != null) {
            for (entry in (it as EnchantmentsImpl).enchants) {
                addEnchant((entry.key as EnchantmentImpl).bukkit, entry.value, true)
            }
        }
        if (it?.showInTooltip == true) {
            removeItemFlags(ItemFlag.HIDE_ENCHANTS)
        } else {
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }
)

internal val enchantableItemMetaConverter = ItemMetaDataKeyConverter<Enchantable>(
    {
        Enchantable(enchantable)
    },
    {
        setEnchantable(it?.value)
    }
)
