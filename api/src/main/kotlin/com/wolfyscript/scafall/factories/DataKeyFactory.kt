package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.identifier.Key

interface DataKeyFactory {

    fun <T : Any, H: DataHolder<*, *>> create(key: Key): DataKey<T, H>

}