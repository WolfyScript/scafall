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

import com.google.common.base.Preconditions
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import org.bukkit.util.Vector
import java.util.*
import java.util.function.Consumer

/**
 * A complex shape that combines multiple shapes together.<br></br>
 * There is no longer limit of how many shapes it can contain.<br></br>
 * However, there is a lower limit of at least 2 shapes, to discourage the use of this shape for single shapes! Single shapes should be used separately!
 */
class ShapeComplexCompound(vararg shapes: Shape) : Shape(KEY) {
    private val shapes: List<Shape>

    /**
     * Only used for Jackson deserialization.
     */
    internal constructor() : this(ShapeCircle(), ShapeCircle())

    init {
        Preconditions.checkArgument(
            shapes != null && shapes.size > 1,
            "Shapes array cannot be null and must contain at least 2 shapes! (For single shapes use the shape directly!)"
        )
        this.shapes = Arrays.asList(*shapes)
    }

    override fun drawVectors(time: Double, drawVector: Consumer<Vector>) {
        shapes.forEach(Consumer { shape: Shape -> shape.drawVectors(time, drawVector) })
    }

    companion object {
        val KEY: Key = scaffoldingKey("complex/compound")
    }
}
