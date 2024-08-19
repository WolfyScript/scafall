package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig

interface ItemsFactory {

    fun createStackConfig(itemKey: Key) : ItemStackConfig


}