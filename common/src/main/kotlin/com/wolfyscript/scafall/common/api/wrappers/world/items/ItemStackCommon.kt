package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.ItemStackDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class ItemStackCommon(mcStack: net.minecraft.world.item.ItemStack) : ItemStackLikeCommon<ItemStack, DataComponentMap.Mutable<ItemStack>>(mcStack), ItemStack {

    override fun snapshot(): ItemStackSnapshot {
        return ItemStackSnapshotCommon(mcStack.copy())
    }

    override val data: DataComponentMap.Mutable<ItemStack> = ItemStackDataComponentMap(this)

}