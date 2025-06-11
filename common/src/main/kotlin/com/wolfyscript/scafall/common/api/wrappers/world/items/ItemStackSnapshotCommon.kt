package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.data.ItemStackSnapshotDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class ItemStackSnapshotCommon(stack: net.minecraft.world.item.ItemStack) : ItemStackLikeCommon<ItemStackSnapshot, DataComponentMap.Immutable<ItemStackSnapshot>>(stack), ItemStackSnapshot {

    @JsonCreator
    private constructor(snbt: String): this(ScafallProvider.get().factories.itemsFactory.createFromSNBT(snbt).unwrap())

    override fun createStack(): ItemStack {
        return ItemStackCommon(mcStack.copy())
    }

    override val data: DataComponentMap.Immutable<ItemStackSnapshot> = ItemStackSnapshotDataComponentMap(this)

}