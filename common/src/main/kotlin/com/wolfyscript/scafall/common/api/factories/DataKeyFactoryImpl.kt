package com.wolfyscript.scafall.common.api.factories

import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.factories.DataKeyFactory
import com.wolfyscript.scafall.identifier.Key
import kotlin.reflect.KClass

class DataKeyFactoryImpl : DataKeyFactory {

    override fun <T : Any, H : DataHolder<*, *>> create(type: KClass<T>, key: Key): DataKey<T, H> {
        return DataKey(type, key)
    }

}