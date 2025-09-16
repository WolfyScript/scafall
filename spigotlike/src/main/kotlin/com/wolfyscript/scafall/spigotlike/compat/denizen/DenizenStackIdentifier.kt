package com.wolfyscript.scafall.spigotlike.compat.denizen

import com.denizenscript.denizen.scripts.containers.core.ItemScriptHelper
import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(DenizenDependency::class)
class DenizenStackIdentifier(
    val displayStack: ScafallItemStack,
    val itemScript: String,
) : ItemStackIdentifier {

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        if (stack.isEmpty) return false
        val otherScript = ItemScriptHelper.getItemScriptNameText(stack.unwrapSpigot()) ?: return false
        return otherScript == itemScript
    }

    override fun create(): ScafallItemStack {
        return displayStack.snapshot().createStack()
    }
}