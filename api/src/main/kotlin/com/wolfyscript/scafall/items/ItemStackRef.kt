package com.wolfyscript.scafall.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

interface ItemStackRef {

    /**
     * The stack this [ItemStackRef] was created from and the [ItemStackIdentifier] parsed. Otherwise, referenced stack; If neither is available, AIR.
     */
    @get:JsonIgnore
    val originalStack: ItemStack

    /**
     * The identifier used to construct this ref, or the identifier parsed from the [originalStack]
     * Null if neither is available.
     */
    val identifier: ItemStackIdentifier?

    /**
     * Parser used to parse the [identifier] from an [ItemStack]
     */
    val parser: ItemStackIdentifier.Parser<*>?

    /**
     * Swaps the parser used to parse the [identifier] from an [ItemStack]
     *
     * @return a new [ItemStackRef] with the new parser
     */
    fun swapParser(parser: ItemStackIdentifier.Parser<*>): ItemStackRef

    fun matches(stack: ItemStack, matchTags: Boolean): Boolean

    fun create(): ItemStack

}