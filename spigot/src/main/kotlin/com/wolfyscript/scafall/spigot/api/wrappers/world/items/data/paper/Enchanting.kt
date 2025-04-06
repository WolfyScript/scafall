package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.EnchantmentsImpl
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.enchanting.EnchantmentImpl
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.Enchantable
import com.wolfyscript.scafall.wrappers.world.items.data.Enchantments
import com.wolfyscript.scafall.wrappers.world.items.enchanting.Enchantment
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments

internal val enchantableConverter = PaperDataAPIConverter(
    {
        val enchantable = unwrap().getData(DataComponentTypes.ENCHANTABLE)
        if (enchantable == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(Enchantable(enchantable.value()))
    }, {
        unwrap().setData(DataComponentTypes.ENCHANTABLE, io.papermc.paper.datacomponent.item.Enchantable.enchantable(it.value))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.ENCHANTABLE)
        Result.success(this to true)
    }
)

internal val enchantmentsConverter = PaperDataAPIConverter<Enchantments>(
    {
        val enchantments = unwrap().getData(DataComponentTypes.ENCHANTMENTS)
        if (enchantments == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(
            EnchantmentsImpl(enchantments.enchantments().mapKeys<org.bukkit.enchantments.Enchantment, Int, Enchantment> { EnchantmentImpl(it.key) }.toMutableMap())
        )
    }, {
        unwrap().setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments((it as EnchantmentsImpl).enchants.mapKeys { (it.key as EnchantmentImpl).bukkit }))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.ENCHANTMENTS)
        Result.success(this to true)
    }
)

internal val storedEnchantmentsConverter = PaperDataAPIConverter<Enchantments>(
    {
        val enchantments = unwrap().getData(DataComponentTypes.STORED_ENCHANTMENTS)
        if (enchantments == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(
            EnchantmentsImpl(enchantments.enchantments().mapKeys<org.bukkit.enchantments.Enchantment, Int, Enchantment> { EnchantmentImpl(it.key) }.toMutableMap())
        )
    }, {
        unwrap().setData(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.itemEnchantments((it as EnchantmentsImpl).enchants.mapKeys { (it.key as EnchantmentImpl).bukkit }))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.STORED_ENCHANTMENTS)
        Result.success(this to true)
    }
)

internal val enchantmentGlintOverrideConverter = PaperDataAPIConverter(
    {
        Result.success(unwrap().getData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE))
    }, {
        unwrap().setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, it)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)
        Result.success(this to true)
    }
)
