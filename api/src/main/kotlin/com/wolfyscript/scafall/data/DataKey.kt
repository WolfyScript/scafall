package com.wolfyscript.scafall.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import kotlin.reflect.KClass

class DataKey<T : Any, V : DataHolder<*, *>>(
    val type: KClass<T>,
    private val key: Key
) : Keyed {

    override fun key(): Key = key

}
