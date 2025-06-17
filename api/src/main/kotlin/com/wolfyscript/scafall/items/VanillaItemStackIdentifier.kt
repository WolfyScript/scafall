package com.wolfyscript.scafall.items

import com.wolfyscript.scafall.wrappers.world.items.ItemStack

interface VanillaItemStackIdentifier : ItemStackIdentifier {

    val stack: ItemStack

    interface Parser : ItemStackIdentifier.Parser<VanillaItemStackIdentifier>

}