package com.wolfyscript.scafall.spigotlike.compat.executableblocks

import com.ssomar.executableblocks.executableblocks.ExecutableBlocksManager
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import kotlin.jvm.optionals.getOrNull

class ExecutableBlocksStackIdentifierParser(override val priority: Int = 1800) : ItemStackIdentifier.Parser<ExecutableBlocksStackIdentifier> {

    private val manager: ExecutableBlocksManager = ExecutableBlocksManager.getInstance()

    override fun from(stack: ItemStackLike): ExecutableBlocksStackIdentifier? {
        val item = manager.getExecutableBlock(stack.unwrapSpigot()).getOrNull() ?: return null
        return ExecutableBlocksStackIdentifier(item.id)
    }
}