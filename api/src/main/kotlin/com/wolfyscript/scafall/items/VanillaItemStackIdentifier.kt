package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.wrappers.world.items.ItemStack

/**
 * A reference to a vanilla item stack.
 */
interface VanillaItemStackIdentifier : ItemStackIdentifier {

    /**
     * The item stack that this identifier references.
     */
    val stack: ItemStack

    /**
     * Parses a [VanillaItemStackIdentifier] from the specified source.
     * This parser matches any given [ItemStack] and should have the lowest priority to not override all the other parsers.
     */
    interface Parser : ItemStackIdentifier.Parser<VanillaItemStackIdentifier>

}