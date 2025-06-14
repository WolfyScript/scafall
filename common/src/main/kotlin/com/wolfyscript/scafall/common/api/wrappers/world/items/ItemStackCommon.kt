package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.data.ItemStackDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import net.minecraft.SharedConstants

class ItemStackCommon private constructor(
    mcStack: net.minecraft.world.item.ItemStack,
    version: Int = SharedConstants.getCurrentVersion().dataVersion.version,
) : ItemStackLikeCommon<ItemStack, DataComponentMap.Mutable<ItemStack>>(mcStack), ItemStack {

    val version: Int = version

    companion object {

        fun fromVanilla(stack: net.minecraft.world.item.ItemStack): ItemStackCommon {
            return ItemStackCommon(stack)
        }

    }

    @JsonCreator
    private constructor(snbt: String, version: Int = SharedConstants.getCurrentVersion().dataVersion.version) : this(
        ScafallProvider.get().factories.itemsFactory.parseFromSNBT(snbt, version).unwrap(),
        version
    )

    override fun snapshot(): ItemStackSnapshot {
        return ItemStackSnapshotCommon(mcStack.copy())
    }

    @JsonIgnore
    override val data: DataComponentMap.Mutable<ItemStack> = ItemStackDataComponentMap(this)

}