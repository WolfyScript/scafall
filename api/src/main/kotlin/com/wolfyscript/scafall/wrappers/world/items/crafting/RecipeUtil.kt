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
package com.wolfyscript.scafall.wrappers.world.items.crafting

import org.apache.commons.lang3.StringUtils
import java.util.*

object RecipeUtil {

    /**
     * Checks if the column of the recipe shape is blocked by items.
     * When the column is empty it will be removed from the shape ArrayList!
     *
     * @param shape  the shape of the recipe
     * @param column the index of the column
     * @return true if the column is blocked
     */
    fun checkColumn(shape: ArrayList<String>, column: Int): Boolean {
        val blocked = shape.stream().anyMatch { s: String -> column < s.length && s[column] != ' ' }
        if (!blocked) {
            for (i in shape.indices) {
                if (column < shape[i].length) {
                    shape[i] = shape[i].substring(0, column) + shape[i].substring(column + 1)
                }
            }
        }
        return blocked
    }

    /**
     * Formats the recipe shape to it's smallest possible size.
     * That means if a recipe that was created inside a 3x3 grid only takes up 2x1
     * this method will shrink the array down from a 3x3 to 2x1 array
     *
     * @param shape the recipe that should be formatted
     * @return the shrunken ArrayList of the recipe shape
     */
    fun formatShape(vararg shape: String): ArrayList<String> {
        val cleared = ArrayList(listOf(*shape))
        val rowIterator = cleared.listIterator()
        while (rowIterator.hasNext()) {
            if (!StringUtils.isBlank(rowIterator.next())) {
                break
            }
            rowIterator.remove()
        }
        while (rowIterator.hasNext()) {
            rowIterator.next()
        }
        while (rowIterator.hasPrevious()) {
            if (!StringUtils.isBlank(rowIterator.previous())) {
                break
            }
            rowIterator.remove()
        }
        if (cleared.isNotEmpty()) {
            var columnBlocked = false
            while (!columnBlocked) {
                if (checkColumn(cleared, 0)) {
                    columnBlocked = true
                }
            }
            columnBlocked = false
            var column = cleared[0].length - 1
            while (!columnBlocked) {
                if (checkColumn(cleared, column)) {
                    columnBlocked = true
                } else {
                    column--
                }
            }
        }
        return cleared
    }
}
