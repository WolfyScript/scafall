package com.wolfyscript.scafall.common.api.wrappers.world.attribute

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.attribute.Attribute

class CommonAttribute(val key: Key) : Attribute {

    override fun key(): Key = key

}