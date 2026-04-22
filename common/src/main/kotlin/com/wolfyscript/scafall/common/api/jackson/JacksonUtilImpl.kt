package com.wolfyscript.scafall.common.api.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.identifier.KeyImpl
import com.wolfyscript.scafall.items.ItemStackRefImpl
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStackCommon
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshotCommon
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackRef
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class JacksonUtilImpl : JacksonUtil {

    private val module = SimpleModule("scafall")

    init {
        module.apply {
            addAbstractTypeMapping(ScafallItemStack::class.java, ScafallItemStackCommon::class.java)
            addAbstractTypeMapping(ItemStackSnapshot::class.java, ItemStackSnapshotCommon::class.java)

            addAbstractTypeMapping(ItemStackRef::class.java, ItemStackRefImpl::class.java)
            addAbstractTypeMapping(Key::class.java, KeyImpl::class.java)
        }
    }

    override fun registerScafallModule(mapper: ObjectMapper): ObjectMapper {
        return mapper.apply { registerModule(module) }
    }
}