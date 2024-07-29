package com.wolfyscript.scaffolding.common.api.registries

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.registry.Registries
import com.wolfyscript.scaffolding.registry.Registry
import kotlin.reflect.KClass

class CommonRegistries : Registries {

    override fun <T : Any> get(key: Key, type: KClass<T>): Registry<T>? {
        TODO("Not yet implemented")
    }

    override fun <T : Any> get(type: KClass<T>): Registry<T>? {
        TODO("Not yet implemented")
    }

}