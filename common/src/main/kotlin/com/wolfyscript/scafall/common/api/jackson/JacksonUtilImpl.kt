package com.wolfyscript.scafall.common.api.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.common.api.identifiers.KeyImpl
import com.wolfyscript.scafall.common.api.items.ItemStackRefImpl
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackSnapshotCommon
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackRef
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class JacksonUtilImpl : JacksonUtil {

    private val module = SimpleModule("scafall")

    init {
        module.apply {
            addAbstractTypeMapping(ItemStack::class.java, ItemStackCommon::class.java)
            addAbstractTypeMapping(ItemStackSnapshot::class.java, ItemStackSnapshotCommon::class.java)

            addAbstractTypeMapping(ItemStackRef::class.java, ItemStackRefImpl::class.java)
            addAbstractTypeMapping(Key::class.java, KeyImpl::class.java)
        }
    }

    override fun registerScafallModule(mapper: ObjectMapper): ObjectMapper {
        mapper.apply {
            registerModule(module)
        }
        return mapper
    }
}