package com.wolfyscript.scafall.spigot.compat.denizen

import com.denizenscript.denizen.scripts.containers.core.ItemScriptHelper
import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(DenizenDependency::class)
class DenizenStackIdentifier(
    val displayStack: ItemStack,
    val itemScript: String,
) : ItemStackIdentifier {

    override fun matches(
        stack: ItemStack,
        matchTags: Boolean,
    ): Boolean {
        if (stack.isEmpty) return false
        val otherScript = ItemScriptHelper.getItemScriptNameText(stack.unwrapSpigot()) ?: return false
        return otherScript == itemScript
    }

    override fun create(): ItemStack {
        return displayStack.snapshot().createStack()
    }
}