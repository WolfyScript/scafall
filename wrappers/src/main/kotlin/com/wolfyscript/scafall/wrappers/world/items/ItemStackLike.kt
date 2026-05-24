package com.wolfyscript.scafall.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.Key

/**
 * The base type of [ScafallItemStack] and [ItemStackSnapshot], combining common properties and functions
 */
interface ItemStackLike {

    /**
     * The id representing the item of this ItemStack.<br></br>
     * Usually e.g. <pre>minecraft:&lt;item_id&gt;</pre>
     *
     * @return The id of the item.
     */
    @get:JsonIgnore
    val item: Key

    /**
     * The stack amount of this ItemStack.
     *
     * @return The stack amount.
     */
    @get:JsonIgnore
    val amount: Int

    @get:JsonIgnore
    val isEmpty: Boolean

    /**
     * Returns the vanilla NBT mojangJson representation of this ItemStackLike.
     * This is multiplatform compatible and can easily be parsed/updated using Minecraft DataFixer.
     */
    @JsonProperty("snbt")
    fun toNBTString() : String

    /**
     * Returns the vanilla NBT binary bytes representation of this ItemStackLike.
     * This is multiplatform compatible and can easily be read/updated using Minecraft DataFixer.
     */
    fun toNBTBytes() : ByteArray

    /**
     * Unwraps this [ItemStackLike] into a vanilla [net.minecraft.world.item.ItemStack].
     *
     * @return The vanilla [net.minecraft.world.item.ItemStack].
     */
    fun unwrap() : net.minecraft.world.item.ItemStack

}