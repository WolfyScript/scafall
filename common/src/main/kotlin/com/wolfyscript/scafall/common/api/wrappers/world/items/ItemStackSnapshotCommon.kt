package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.ItemStackSnapshotDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class ItemStackSnapshotCommon(stack: net.minecraft.world.item.ItemStack) : ItemStackLikeCommon<ItemStackSnapshot, DataComponentMap.Immutable<ItemStackSnapshot>>(stack), ItemStackSnapshot {

    override fun createStack(): ItemStack {
        return ItemStackCommon(mcStack.copy())
    }

    override val data: DataComponentMap.Immutable<ItemStackSnapshot> = ItemStackSnapshotDataComponentMap(this)

}