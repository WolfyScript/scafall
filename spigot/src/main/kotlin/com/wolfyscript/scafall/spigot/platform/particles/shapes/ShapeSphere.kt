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
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import org.bukkit.util.Vector
import java.util.function.BiFunction
import java.util.function.Consumer
import kotlin.math.cos
import kotlin.math.sin

class ShapeSphere(
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
                Direction.X_AXIS -> BiFunction { i: Double?, j: Double? ->
                    val x = radius * cos(j!!)
                    val y = radius * sin(i!!) * sin(j)
                    val z = radius * cos(i) * sin(j)
                    Vector(x, y, z)
                }

                Direction.Y_AXIS -> BiFunction { i: Double?, j: Double? ->
                    val x = radius * cos(i!!) * sin(
                        j!!
                    )
                    val y = radius * cos(j)
                    val z = radius * sin(i) * sin(j)
                    Vector(x, y, z)
                }

                Direction.Z_AXIS -> BiFunction { i: Double?, j: Double? ->
                    val x = radius * cos(i!!) * sin(
                        j!!
                    )
                    val y = radius * sin(i) * sin(j)
                    val z = radius * cos(j)
                    Vector(x, y, z)
                }

                null -> BiFunction { _, _ -> Vector(0,0,0) }
            }
        }

    @JsonIgnore
    private var createVector: BiFunction<Double, Double, Vector>? = null

    /**
     * Only used for Jackson deserialization.
     */
    internal constructor() : this(1.0, 3, Direction.Y_AXIS)

    init {
        this.direction = direction
    }

    override fun drawVectors(time: Double, drawVector: Consumer<Vector>) {
        var i = 0.0
        while (i <= 2 * Math.PI) {
            var j = 0.0
            while (j <= Math.PI * 2) {
                drawVector.accept(createVector!!.apply(j, i))
                j += Math.PI / resolution
            }
            i += Math.PI / resolution
        }
    }

    companion object {
        val KEY: Key = defaultKey("sphere")
    }
}
