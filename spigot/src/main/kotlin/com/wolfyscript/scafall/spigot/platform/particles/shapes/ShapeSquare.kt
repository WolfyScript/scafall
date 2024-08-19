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
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import org.bukkit.util.Vector
import java.util.function.Consumer
import java.util.function.Function

class ShapeSquare(@get:JsonGetter val radius: Double, private var pointsPerSide: Int, direction: Direction) :
    Shape(KEY) {
    @get:JsonGetter
    @set:JsonSetter
    var direction: Direction? = null
        private set(direction) {
            field = direction
            createVector = when (direction) {
                Direction.X_AXIS -> Function { t: Double ->
                    arrayOf(
                        Vector(
                            0.0, radius - t,
                            radius
                        ),
                        Vector(0.0, radius - t, -radius),
                        Vector(0.0, radius, radius - t),
                        Vector(0.0, -radius, radius - t)
                    )
                }

                Direction.Y_AXIS -> Function { t: Double ->
                    arrayOf(
                        Vector(
                            radius - t, 0.0,
                            radius
                        ),
                        Vector(radius - t, 0.0, -radius),
                        Vector(radius, 0.0, radius - t),
                        Vector(-radius, 0.0, radius - t)
                    )
                }

                Direction.Z_AXIS -> Function { t: Double ->
                    arrayOf(
                        Vector(
                            radius - t,
                            radius, 0.0
                        ),
                        Vector(radius - t, -radius, 0.0),
                        Vector(radius, radius - t, 0.0),
                        Vector(-radius, radius - t, 0.0)
                    )
                }

                null -> Function { emptyArray<Vector>() }
            }
        }

    @JsonIgnore
    private var createVector: Function<Double, Array<Vector>>? = null

    /**
     * Only used for Jackson deserialization.
     */
    internal constructor() : this(1.0, 3, Direction.Y_AXIS)

    init {
        this.direction = direction
    }

    override fun drawVectors(time: Double, drawVector: Consumer<Vector>) {
        val pointIncrease = radius * 2 / (pointsPerSide - 1)
        var i = 0.0
        while (i <= radius * 2) {
            val sides = createVector!!.apply(i)
            for (side in sides) {
                drawVector.accept(side)
            }
            i += pointIncrease
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
        val KEY: Key = defaultKey("square")
    }
}
