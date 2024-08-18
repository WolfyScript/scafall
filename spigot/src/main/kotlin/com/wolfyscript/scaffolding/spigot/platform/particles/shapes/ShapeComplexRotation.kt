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
package com.wolfyscript.scaffolding.spigot.platform.particles.shapes

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.spigot.platform.math.MathUtil.degreeToRadiansVector
import com.wolfyscript.scaffolding.spigot.platform.math.MathUtil.getRotationAngles
import com.wolfyscript.scaffolding.spigot.platform.math.MathUtil.rotate
import org.bukkit.util.Vector
import java.util.*
import java.util.function.Consumer

/**
 * A complex shape that rotates the contained shape with the given settings.<br></br>
 *
 * There is no limit at what the sub shape must be. Any kind of [Shape] can be used, including complex shapes like [ShapeComplexRotation], [ShapeComplexCompound], etc.
 */
class ShapeComplexRotation @JvmOverloads constructor(angle: Vector, shape: Shape, angleMultiplier: Vector = Vector()) :
    Shape(KEY) {
    private val angleMultiplier: Vector =
        Objects.requireNonNull(angleMultiplier, "Angle multiplier cannot be null!")
    private val angle: Vector =
        Objects.requireNonNull(angle, "Angle cannot be null!")

    private val shape: Shape =
        Objects.requireNonNull(
            shape,
            "Shape cannot be null!"
        )

    /**
     * Only used for Jackson deserialization.
     */
    internal constructor() : this(Vector(), ShapeCircle())

    constructor(shape: Shape) : this(Vector(), shape)

    override fun drawVectors(time: Double, drawVector: Consumer<Vector>) {
        val rotation = Vector(
            time * angleMultiplier.x + angle.x,
            time * angleMultiplier.y + angle.y,
            time * angleMultiplier.z + angle.z
        )
        val angles = getRotationAngles(degreeToRadiansVector(rotation))
        shape.drawVectors(time) { vec: Vector? ->
            rotate(vec!!, angles)
            drawVector.accept(vec)
        }
    }

    companion object {
        val KEY: Key = scaffoldingKey("complex/rotation")
    }
}
