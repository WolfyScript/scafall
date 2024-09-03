package com.wolfyscript.scafall.sponge.api.wrappers.world.items

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.wrappers.SpongeRefWrapper
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.spongepowered.api.item.ItemTypes

class ItemStackWrapper(ref: org.spongepowered.api.item.inventory.ItemStack) : SpongeRefWrapper<org.spongepowered.api.item.inventory.ItemStack>(ref), ItemStack {

    override val item: Key
        get() = ItemTypes.registry().valueKey(ref.type()).toAPI()
    override val amount: Int
        get() = ref.quantity()

    override fun snapshot(): ItemStackConfig {
        TODO("Not yet implemented")
    }

    override fun data(): DataComponentMap<ItemStack> {
        TODO("Not yet implemented")
    }
}