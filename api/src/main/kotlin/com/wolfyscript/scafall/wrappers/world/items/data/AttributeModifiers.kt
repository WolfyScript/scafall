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
package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.attribute.Attribute
import com.wolfyscript.scafall.wrappers.world.attribute.AttributeModifier

data class AttributeModifiers(
    val modifiers: List<Entry>,
    override val showInTooltip: Boolean
) : TooltipApplicable {

    data class Entry(
        val attribute: Attribute,
        val modifier: AttributeModifier
    )

    data class Modifier(
        val type: Key,
        val slot: Slot,
        val id: Key,
        val amount: Double,
        val operation: Operation
    ) {

        enum class Slot(val id: String) {

            ANY("any"),
            HAND("hand"),
            ARMOR("armor"),
            MAIN_HAND("mainhand"),
            OFF_HAND("offhand"),
            HEAD("head"),
            CHEST("chest"),
            LEGS("legs"),
            FEET("feet"),
            BODY("body")

        }

        enum class Operation(val id: String) {

            ADD_VALUE("add_value"),
            ADD_MULTIPLIED_BASE("add_multiplied_base"),
            ADD_MULTIPLIED_TOTAL("add_multiplied_total"), ;

        }
    }


}
