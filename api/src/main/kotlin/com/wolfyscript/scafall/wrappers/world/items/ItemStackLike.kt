package com.wolfyscript.scafall.wrappers.world.items

import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.identifier.Key

interface ItemStackLike : DataHolder<ItemStack> {

    /**
     * The id representing the item of this ItemStack.<br></br>
     * Usually e.g. <pre>minecraft:&lt;item_id&gt;</pre>
     *
     * @return The id of the item.
     */
    val item: Key

    /**
     * The stack amount of this ItemStack.
     *
     * @return The stack amount.
     */
    val amount: Int

}