package com.wolfyscript.scafall.common.api.data

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.MinecraftDataComponentWrapper
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack

data class MinecraftItemStackDataComponentConverter<T : Any, I: Any>(
    val dataComponentType: DataComponentType<I>,
    val fetcher: I.(ItemStack) -> MinecraftDataComponentWrapper<T, I>?,
    val applier: ItemStack.(MinecraftDataComponentWrapper<T, I>) -> I = { it.toMinecraft() },
    val remover: ItemStack.() -> Unit = { remove(dataComponentType) }
)
