package com.wolfyscript.scaffolding.factories

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.wrappers.world.items.ItemStackConfig

interface ItemsFactory {

    fun createStackConfig(itemKey: Key) : ItemStackConfig


}