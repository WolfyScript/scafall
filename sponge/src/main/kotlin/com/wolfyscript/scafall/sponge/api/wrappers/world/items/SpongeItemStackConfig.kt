package com.wolfyscript.scafall.sponge.api.wrappers.world.items

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

class SpongeItemStackConfig(itemId: String) : ItemStackConfig(itemId) {

    override fun constructItemStack(
        context: EvalContext,
        miniMessage: MiniMessage?,
        tagResolvers: TagResolver
    ): com.wolfyscript.scafall.wrappers.world.items.ItemStack? {
        val spongeStack : ItemStack = ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(itemId)).getOrNull()?.let { ItemStack.of(it) } ?: ItemStack.empty()

        return spongeStack.wrap()
    }
}