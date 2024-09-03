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
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.operator.BoolOperator
import com.wolfyscript.scafall.eval.operator.BoolOperatorConst
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderIntegerConst
import com.wolfyscript.scafall.eval.value_provider.ValueProviderStringConst
import com.wolfyscript.scafall.nbt.NBTTagConfigCompound
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.Collections

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
    val itemId: String
) {
    /* ********************
     * Common NBT Settings
     * ********************/
    /**
     * The display name of the stack.
     * Direct support for Adventure tags.
     */
    var name: ValueProvider<String>? = null

    fun name(name: String) {
        this.name = ValueProviderStringConst(name)
    }

    /**
     * The lore of the stack. Direct support for Adventure tags.
     */
    var lore: List<ValueProvider<String>> = ArrayList()
        get() = Collections.unmodifiableList(field)

    var amount: ValueProvider<Int> = ValueProviderIntegerConst(1)
    var repairCost: ValueProvider<Int> = ValueProviderIntegerConst(0)
    var damage: ValueProvider<Int> = ValueProviderIntegerConst(0)
    var unbreakable: BoolOperator = BoolOperatorConst(false)
    var customModelData: ValueProvider<Int>? = ValueProviderIntegerConst(0)
    var enchants: Map<String, ValueProvider<Int>> = HashMap()
        get() = Collections.unmodifiableMap(field)

    /* ********************
     * Unhandled NBT Tags
     * ********************/
    @get:JsonGetter("nbt")
    var nbt: NBTTagConfigCompound = NBTTagConfigCompound(null)

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
