package com.wolfyscript.scafall.sponge.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.CommonDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.SpongeRefWrapper
import com.wolfyscript.scafall.sponge.api.wrappers.wrap
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.spongepowered.api.item.ItemTypes

class ItemStackWrapper(ref: org.spongepowered.api.item.inventory.ItemStack) : SpongeRefWrapper<org.spongepowered.api.item.inventory.ItemStack>(ref), ItemStack {

    private val componentMap = CommonDataComponentMap(this)

    override val item: Key = ItemTypes.registry().valueKey(ref.type()).toAPI()
    override val amount: Int = ref.quantity()

    override fun snapshot(): ItemStackSnapshot {
        return ref.asImmutable().wrap()
    }

    override fun data(): DataComponentMap<ItemStack> = componentMap

}