package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.identifier.Key
import kotlin.reflect.KClass

interface DataKeyFactory {

    fun <T : Any, H: DataHolder<*, *>> create(type: KClass<T>, key: Key): DataKey<T, H>

}