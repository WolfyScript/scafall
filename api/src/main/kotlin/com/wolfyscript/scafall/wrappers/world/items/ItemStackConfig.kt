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
import com.wolfyscript.scafall.identifier.Keyed
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * A cross-platform ItemStack configuration using the jackson library.
 *
 * The [stack] is stored as vanilla SNBT in the config and used as the base on which changes are applied.
 * [Overrides][Override] can be used to manipulate the [stack] upon creating it.
 *
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
interface ItemStackConfig {

    /**
     * The id of the item in the `<namespace>:<item_key>` format.
     */
    @get:JsonProperty("stack")
    val stack: ItemStackSnapshot

    /**
     * The amount of the created stack. The value may be computed from other sources.
     */
    var amount: ValueProvider<Int>

    /**
     * The overrides that are loaded and will be applied to the stack.
     */
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

    /**
     * An override specifies settings that are applied to the [ItemStack] created from an [ItemStackConfig].
     * Therefore, an override is designed to be serializable and configurable.
     *
     * The values applied by an override can also adapt to the given [EvalContext].
     *
     * They should not be confused with the vanilla Data Components. Overrides use Data Components internally to apply data to the
     * ItemStack, but they are not necessarily 1:1 wrappers, as they can be used for more complex custom behavior.
     */
    @JsonTypeResolver(KeyedTypeResolver::class)
    @JsonTypeIdResolver(KeyedTypeIdResolver::class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonPropertyOrder(value = ["type"])
    interface Override : Keyed {

        /**
         * The type of the override
         */
        val type: Key

        /**
         * Applies this override to the [ItemStack] that is being constructed.
         */
        fun applyTo(itemStack: ItemStack, context: EvalContext, miniMessage: MiniMessage?, tagResolvers: TagResolver)

    }

}
