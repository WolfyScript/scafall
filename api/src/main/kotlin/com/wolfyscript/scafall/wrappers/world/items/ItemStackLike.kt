package com.wolfyscript.scafall.wrappers.world.items

import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.identifier.Key

/**
 * The base type of [ItemStack] and [ItemStackSnapshot], combining common properties and functions
 */
interface ItemStackLike<H: ItemStackLike<H, M>, M: DataComponentMap<H>> : DataHolder<H, M> {

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

    /**
     * Returns the vanilla NBT mojangJson representation of this ItemStackLike.
     * This is multiplatform compatible and can easily be parsed/updated using Minecraft DataFixer.
     */
    fun toNBTString() : String

    /**
     * Returns the vanilla NBT binary bytes representation of this ItemStackLike.
     * This is multiplatform compatible and can easily be read/updated using Minecraft DataFixer.
     */
    fun toNBTBytes() : ByteArray

}