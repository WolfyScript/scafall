package com.wolfyscript.scafall.spigot.compat.eco

import com.willfp.eco.core.items.Items
import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(EcoDependency::class)
class EcoStackIdentifier(
    val itemKey: Key
) : ItemStackIdentifier {

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        if (stack.isEmpty) return false
        val item = Items.getCustomItem(stack.unwrapSpigot()) ?: return false
        return item.key == itemKey.bukkit()
    }

    override fun create(): ItemStack {
        return Items.lookup(itemKey.toString()).item.wrap()
    }
}