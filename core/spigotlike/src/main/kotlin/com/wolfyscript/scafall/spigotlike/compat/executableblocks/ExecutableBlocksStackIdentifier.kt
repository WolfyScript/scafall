package com.wolfyscript.scafall.spigotlike.compat.executableblocks

import com.ssomar.executableblocks.executableblocks.ExecutableBlocksManager
import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(ExecutableBlocksDependency::class)
class ExecutableBlocksStackIdentifier(
    val id: String,
) : ItemStackIdentifier {

    val manager: ExecutableBlocksManager = ExecutableBlocksManager.getInstance()

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        if (stack.isEmpty) return false
        val item = manager.getExecutableBlock(stack.unwrapSpigot()).getOrNull() ?: return false
        return item.id == id
    }

    override fun create(): ScafallItemStack {
        return manager.getExecutableBlock(id).map { it.buildItem(1, Optional.empty()) }.getOrNull()?.wrap() ?: net.minecraft.world.item.ItemStack.EMPTY.wrap()
    }
}