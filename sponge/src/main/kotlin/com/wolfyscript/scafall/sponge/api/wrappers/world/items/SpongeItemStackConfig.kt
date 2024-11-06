package com.wolfyscript.scafall.sponge.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.SnapshotDataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.sponge.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import kotlin.jvm.optionals.getOrNull

class SpongeItemStackConfig(itemId: String) : ItemStackConfig(itemId, SnapshotDataComponentMap()) {

    override fun constructItemStack(
        context: EvalContext,
        miniMessage: MiniMessage?,
        tagResolvers: TagResolver
    ): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        val spongeStack : ItemStack = ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(itemId)).getOrNull()?.let { ItemStack.of(it) } ?: ItemStack.empty()

        val wrappedStack = spongeStack.wrap()

        for (dataKey in data().keys()) {
            applyDataKey(wrappedStack, dataKey)
        }

        return wrappedStack
    }

    private fun <T: Any> applyDataKey(stack: com.wolfyscript.scafall.wrappers.world.items.ItemStack, dataKey: DataKey<T, com.wolfyscript.scafall.wrappers.world.items.ItemStack>) {
        data().get(dataKey)?.let {
            dataKey.writeTo(it, stack)
        }
    }

}