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
package com.wolfyscript.scafall.spigot.platform.world.items.meta

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.common.base.Preconditions
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import com.wolfyscript.scafall.spigot.platform.world.items.ItemBuilder
import java.util.*

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class MetaSettings {
    private val checks: MutableList<Meta> =
        ArrayList()

    /**
     * @param meta The [Meta] to add to the list of checks. Cannot be null, and must not have the [Option.IGNORE], or it will throw an exception!
     */
    fun addCheck(meta: Meta) {
        Objects.requireNonNull(meta, "Meta check cannot be null!")
        Preconditions.checkArgument(
            meta.option != Option.IGNORE,
            "Deprecated option! Ignored check cannot be added!"
        )
        checks.add(meta)
    }

    fun clearChecks() {
        checks.clear()
    }

    fun getChecks(): List<Meta> {
        return java.util.List.copyOf(checks)
    }

    @get:JsonIgnore
    val isEmpty: Boolean
        get() = checks.isEmpty()

    fun check(itemOther: ItemBuilder?, item: ItemBuilder?): Boolean {
        return true
    }

    fun check(item: CustomItem, itemOther: ItemBuilder): Boolean {
        return checks.stream().allMatch { meta: Meta -> meta.check(item, itemOther) }
    }

    enum class Option {
        EXACT,

        /**
         * This option was originally used to indicate if the meta check should be active.
         * Now it is no longer used (Only to convert old data), because checks that are not added to the settings are well... not checked anyway.
         */
        @Deprecated("")
        IGNORE,
        HIGHER,
        HIGHER_EXACT,
        LOWER,
        LOWER_EXACT,
        HIGHER_LOWER
    }

    companion object {
        const val CHECKS_KEY: String = "checks"
    }
}
