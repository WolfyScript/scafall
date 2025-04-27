/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scafall.wrappers.world.items

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeResolver
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.Key
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * A cross-platform ItemStack configuration using the jackson library.
 *
 *
 * @param <I> The native ItemStackType
</I> */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
interface ItemStackConfig {

    /**
     * The id of the item in the `<namespace>:<item_key>` format.
     */
    @get:JsonProperty("stack")
    val stack: ItemStackSnapshot

    var amount: ValueProvider<Int>

    @get:JsonIgnore
    val overrides: Map<Key, Override>

    /**
     * Constructs the implementation specific ItemStack from the settings.
     * The context allows settings to use contextual data to create the ItemStack data.
     *
     * @param context The context to use.
     * @return The constructed ItemStack.
     */
    fun constructItemStack(
        context: EvalContext = EvalContext(),
        miniMessage: MiniMessage? = MiniMessage.miniMessage(),
        tagResolvers: TagResolver = TagResolver.empty(),
    ): ItemStack?

    @JsonTypeResolver(KeyedTypeResolver::class)
    @JsonTypeIdResolver(KeyedTypeIdResolver::class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonPropertyOrder(value = ["type"])
    interface Override {

        val type: Key

        /**
         * Applies this override to the [ItemStack] that is being constructed.
         */
        fun applyTo(itemStack: ItemStack, context: EvalContext, miniMessage: MiniMessage?, tagResolvers: TagResolver)

    }

}
