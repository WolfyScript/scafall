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
package com.wolfyscript.scafall.spigot.platform.particles.shapes

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonSetter
import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import org.bukkit.util.Vector
import java.util.function.Consumer
import kotlin.math.max
import kotlin.math.min

class ShapeCube(@get:JsonGetter val radius: Double, private var pointsPerSide: Int) : Shape(
    KEY
) {
    /**
     * Only used for Jackson deserialization.
     */
    internal constructor() : this(1.0, 3)

    override fun drawVectors(time: Double, drawVector: Consumer<Vector>) {
        val pointIncrease = radius * 2 / (pointsPerSide - 1)
        val corner1 = Vector(radius, radius, radius)
        val corner2 = Vector(-radius, -radius, -radius)

        val minX = min(corner1.x, corner2.x)
        val minY = min(corner1.y, corner2.y)
        val minZ = min(corner1.z, corner2.z)
        val maxX = max(corner1.x, corner2.x)
        val maxY = max(corner1.y, corner2.y)
        val maxZ = max(corner1.z, corner2.z)

        var x = minX
        while (x <= maxX) {
            var y = minY
            while (y <= maxY) {
                var z = minZ
                while (z <= maxZ) {
                    var components = 0
                    if (x == minX || x == maxX) components++
                    if (y == minY || y == maxY) components++
                    if (z == minZ || z == maxZ) components++
                    if (components >= 2) {
                        drawVector.accept(Vector(x, y, z))
                    }
                    z += pointIncrease
                }
                y += pointIncrease
            }
            x += pointIncrease
        }
    }

    @JsonSetter
    private fun setPointsPerSide(pointsPerSide: Int) {
        Preconditions.checkArgument(pointsPerSide > 1, "Points per side must be at least 2!")
        this.pointsPerSide = pointsPerSide
    }

    @JsonGetter
    fun getPointsPerSide(): Int {
        return pointsPerSide
    }

    companion object {
        val KEY: Key = defaultKey("cube")
    }
}
