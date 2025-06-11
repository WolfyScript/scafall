package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.data.ItemStackDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class ItemStackCommon(mcStack: net.minecraft.world.item.ItemStack) : ItemStackLikeCommon<ItemStack, DataComponentMap.Mutable<ItemStack>>(mcStack), ItemStack {

    @JsonCreator
    private constructor(snbt: String): this(ScafallProvider.get().factories.itemsFactory.createFromSNBT(snbt).unwrap())

    override fun snapshot(): ItemStackSnapshot {
        return ItemStackSnapshotCommon(mcStack.copy())
    }

    override val data: DataComponentMap.Mutable<ItemStack> = ItemStackDataComponentMap(this)

}