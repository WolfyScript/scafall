package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.identifier.registerKeyTypeMappings
import com.wolfyscript.scafall.items.ItemStackRef
import com.wolfyscript.scafall.items.ItemStackRefImpl
import com.wolfyscript.scafall.wrappers.registerStackTypeMapping

class JacksonUtilImpl : JacksonUtil {

    private val module = SimpleModule("scafall")

    init {
        module.apply {
            addAbstractTypeMapping(ItemStackRef::class.java, ItemStackRefImpl::class.java)
            registerStackTypeMapping()
            registerKeyTypeMappings()
        }
    }

    override fun registerScafallModule(mapper: ObjectMapper): ObjectMapper {
        return mapper.apply { registerModule(module) }
    }
}