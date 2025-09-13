package com.wolfyscript.scafall.spigot.compat.executableitems

import com.ssomar.score.api.executableitems.ExecutableItemsAPI
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import kotlin.jvm.optionals.getOrNull

class ExecutableItemsStackIdentifierParser(override val priority: Int = 1800) : ItemStackIdentifier.Parser<ExecutableItemsStackIdentifier> {

    private val manager = ExecutableItemsAPI.getExecutableItemsManager()

    override fun from(stack: ItemStackLike): ExecutableItemsStackIdentifier? {
        val item = manager.getExecutableItem(stack.unwrapSpigot()).getOrNull() ?: return null
        return ExecutableItemsStackIdentifier(item.id)
    }
}