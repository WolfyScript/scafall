package com.wolfyscript.scaffolding.registry

import com.wolfyscript.scaffolding.identifier.Key
import kotlin.reflect.KClass

interface Registries {

    fun <T : Any> get(key: Key, type: KClass<T>): Registry<T>?

    fun <T : Any> get(type: KClass<T>): Registry<T>?

}