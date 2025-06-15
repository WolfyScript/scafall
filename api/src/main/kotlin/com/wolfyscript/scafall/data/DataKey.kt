package com.wolfyscript.scafall.data

import com.wolfyscript.scafall.identifier.Key
import kotlin.reflect.KClass

class DataKey<T : Any, V : DataHolder<*, *>>(
    val type: KClass<T>,
    val key: Key
)
