package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack

/**
 * A reference to a vanilla item stack.
 */
interface VanillaItemStackIdentifier : ItemStackIdentifier {

    /**
     * The item stack that this identifier references.
     */
    val stack: ScafallItemStack

    /**
     * Parses a [VanillaItemStackIdentifier] from the specified source.
     * This parser matches any given [ScafallItemStack] and should have the lowest priority to not override all the other parsers.
     */
    interface Parser : ItemStackIdentifier.Parser<VanillaItemStackIdentifier>

}