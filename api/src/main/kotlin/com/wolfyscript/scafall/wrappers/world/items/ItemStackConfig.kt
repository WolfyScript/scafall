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

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.databind.JsonNode
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderIntegerConst
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.nbt.NBTTagConfigCompound
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * A cross-platform ItemStack configuration using the jackson library.
 *
 *
 * @param <I> The native ItemStackType
</I> */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
abstract class ItemStackConfig(
    /**
     * The id of the item in the `<namespace>:<item_key>` format.
     */
    @JsonProperty("item") val itemId: String,
    private val dataComponentMap: DataComponentMap<ItemStack>
) : DataHolder<ItemStack> {

    override fun data(): DataComponentMap<ItemStack> = dataComponentMap

    @JsonSetter("data_components")
    internal fun readDataComponents(raw: Map<String, JsonNode>) {
        val keysRegistry = ScafallProvider.get().registries.itemDataKeyRegistry
        for ((rawKey, value) in raw) {
            val key = Key.parse(rawKey)
            keysRegistry[key]?.let {
                try {
                    val obj = JacksonUtil.objectMapper.convertValue(value, it.type.java)
                    dataComponentMap.set(it, obj) // When this is reached the type is correct, because otherwise the deserialization would fail
                } catch (e: IllegalArgumentException) {
                    // TODO: Present the error e.g. log it
                }
            }
        }
    }

    @JsonGetter("data_components")
    internal fun writeDataComponents() : Map<String, Any> {
        return TODO()
    }

    var amount: ValueProvider<Int> = ValueProviderIntegerConst(1)

    /**
     * Constructs the implementation specific ItemStack from the settings.
     * The context allows settings to use contextual data to create the ItemStack data.
     *
     * @param context The context to use.
     * @return The constructed ItemStack.
     */
    abstract fun constructItemStack(
        context: EvalContext = EvalContext(),
        miniMessage: MiniMessage? = MiniMessage.miniMessage(),
        tagResolvers: TagResolver = TagResolver.empty()
    ): ItemStack?

}
