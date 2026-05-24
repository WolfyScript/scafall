package com.wolfyscript.scafall.spigotlike.compat.mmoitems

import com.fasterxml.jackson.annotation.JsonCreator
import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.Type

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(MMOItemsDependency::class)
class MMOItemsStackIdentifier(private val type: Type, val itemId: String) : ItemStackIdentifier {

    @JsonCreator
    constructor(typeId: String, itemId: String) : this(MMOItems.plugin.types.get(typeId) ?: error("Could not find MMOItems Type $typeId"), itemId)

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        val stackType = MMOItems.getType(stack.unwrapSpigot()) ?: return false
        val stackItemId = MMOItems.getID(stack.unwrapSpigot()) ?: return false
        return stackType == type && stackItemId == itemId
    }

    override fun create(): ScafallItemStack {
        val item = MMOItems.plugin.getMMOItem(type, itemId) ?: return net.minecraft.world.item.ItemStack.EMPTY.wrap()
        return item.newBuilder().buildSilently().wrap()
    }
}