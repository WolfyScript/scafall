package com.wolfyscript.scafall.items

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.wolfyscript.scafall.config.jackson.RegistryKeyTypeIdResolver
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(RegistryKeyTypeIdResolver::class)
@JsonPropertyOrder("type")
interface ItemStackIdentifier {

    fun matches(stack: ItemStackLike<*,*>, matchTags: Boolean): Boolean

    fun create(): ItemStack

    interface Parser<T : ItemStackIdentifier> {

        val priority: Int

        fun from(stack: ItemStackLike<*,*>): T?

    }

}