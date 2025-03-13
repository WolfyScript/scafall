package com.wolfyscript.scafall.data

import com.wolfyscript.scafall.registry.Registry
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

interface ItemStackDataComponentConverter<T: Any> : DataComponentConverter<T, ItemStackLike<*,*>, ItemStack>

interface ItemStackDataComponentConverterRegistry : Registry<ItemStackDataComponentConverter<*>>
