package com.wolfyscript.scafall.spigotlike.compat.executableblocks

import com.ssomar.score.api.executableblocks.ExecutableBlocksAPI
import com.ssomar.score.api.executableblocks.config.ExecutableBlocksManagerInterface
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import kotlin.jvm.optionals.getOrNull

class ExecutableBlocksStackIdentifierParser(override val priority: Int = 1800) : ItemStackIdentifier.Parser<ExecutableBlocksStackIdentifier> {

    private val manager: ExecutableBlocksManagerInterface = ExecutableBlocksAPI.getExecutableBlocksManager()

    override fun from(stack: ItemStackLike): ExecutableBlocksStackIdentifier? {
        val item = manager.getExecutableBlock(stack.unwrapSpigot()).getOrNull() ?: return null
        return ExecutableBlocksStackIdentifier(item.id)
    }
}