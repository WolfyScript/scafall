package com.wolfyscript.scafall.wrappers

import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshotImpl
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStackImpl

fun SimpleModule.registerStackTypeMapping() {
    addAbstractTypeMapping(ScafallItemStack::class.java, ScafallItemStackImpl::class.java)
    addAbstractTypeMapping(ItemStackSnapshot::class.java, ItemStackSnapshotImpl::class.java)
}