package com.wolfyscript.scafall.spigotlike.compat.itemsadder

import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import dev.lone.itemsadder.api.CustomStack

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(ItemsAdderDependency::class)
class ItemsAdderStackIdentifier(
    val id: String
) : ItemStackIdentifier {

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        val otherId = CustomStack.byItemStack(stack.unwrapSpigot())?.namespacedID ?: return false
        return otherId == id
    }

    override fun create(): ScafallItemStack {
        val IAStack = CustomStack.getInstance(id) ?: return net.minecraft.world.item.ItemStack.EMPTY.wrap()
        return IAStack.itemStack.wrap()
    }
}