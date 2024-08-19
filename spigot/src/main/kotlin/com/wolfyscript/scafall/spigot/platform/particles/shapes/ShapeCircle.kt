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
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import org.bukkit.util.Vector
import java.util.function.Consumer
import java.util.function.Function
import kotlin.math.cos
import kotlin.math.sin

class ShapeCircle(
    @get:JsonGetter val radius: Double,
    @set:JsonSetter @get:JsonGetter var resolution: Int,
    direction: Direction
) :
    Shape(KEY) {
    @get:JsonGetter
    @set:JsonSetter
    var direction: Direction? = null
        private set(direction) {
            field = direction
            createVector = when (direction) {
                Direction.X_AXIS -> Function { t: Double? ->
                    val y = radius * sin(t!!)
                    val z = radius * cos(t)
                    Vector(0.0, y, z)
                }

                Direction.Y_AXIS -> Function { t: Double? ->
                    val x = radius * cos(t!!)
                    val z = radius * sin(t)
                    Vector(x, 0.0, z)
                }

                Direction.Z_AXIS -> Function { t: Double? ->
                    val x = radius * cos(t!!)
                    val y = radius * sin(t)
                    Vector(x, y, 0.0)
                }

                else -> Function { Vector() }
            }
        }

    @JsonIgnore
    private var createVector: Function<Double, Vector>? = null

    /**
     * Only used for Jackson deserialization.
     */
    internal constructor() : this(1.0, 3, Direction.Y_AXIS)

    init {
        this.direction = direction
    }

    override fun drawVectors(time: Double, drawVector: Consumer<Vector>) {
        var i = 0.0
        while (i <= Math.PI * 2) {
            drawVector.accept(createVector!!.apply(i))
            i += Math.PI / resolution
        }
    }

    companion object {
        val KEY: Key = scaffoldingKey("circle")
    }
}
