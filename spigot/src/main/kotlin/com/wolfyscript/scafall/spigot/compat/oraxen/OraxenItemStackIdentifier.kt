package com.wolfyscript.scafall.spigot.compat.oraxen

import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import io.th0rgal.oraxen.api.OraxenItems

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(OraxenDependency::class)
class OraxenItemStackIdentifier(
    val id: String,
) : ItemStackIdentifier {

    override fun matches(
        stack: ItemStackLike<*, *>,
        matchTags: Boolean,
    ): Boolean {
        val otherId = OraxenItems.getIdByItem(stack.unwrapSpigot()) ?: return false
        return otherId == id
    }

    override fun create(): ItemStack {
        val item = OraxenItems.getItemById(id)
        return item.build().wrap()
    }

}