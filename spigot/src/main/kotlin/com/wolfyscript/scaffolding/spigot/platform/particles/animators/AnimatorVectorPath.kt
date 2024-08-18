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
package com.wolfyscript.scaffolding.spigot.platform.particles.animators

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.Preconditions
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.spigot.platform.math.MathUtil.rotateAroundAxisX
import com.wolfyscript.scaffolding.spigot.platform.math.MathUtil.rotateAroundAxisY
import com.wolfyscript.scaffolding.spigot.platform.particles.ParticleEffect
import com.wolfyscript.scaffolding.spigot.platform.particles.shapes.Shape
import com.wolfyscript.scaffolding.spigot.platform.particles.timer.Timer
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * This animator draws a particle shape with the given direction and rotation.
 */
class AnimatorVectorPath @JvmOverloads constructor(
    shape: Shape,
    path: Map<Double, Vector>,
    rotateToDirection: Boolean = true
) :
    Animator(KEY) {
    private val shape: Shape
    private val path: Map<Double, Vector>
    private val rotateToDirection: Boolean

    @JsonCreator
    constructor(@JsonProperty("shape") shape: Shape) : this(shape, java.util.Map.of<Double, Vector>(0.0, Vector()))

    init {
        Preconditions.checkArgument(
            path != null && !path.isEmpty(),
            "Path cannot be null and must contain at least one vector!"
        )
        this.shape = Objects.requireNonNull(shape, "Shape cannot be null!")
        this.path = path
        this.rotateToDirection = rotateToDirection
    }

    override fun draw(timer: Timer.Runner, effect: ParticleEffect?, origin: Location, player: Player?) {
        val previousTime = timer.time
        val time = timer.increase()
        val vector = getVector(time)
        if (rotateToDirection) {
            //Calculate the direction vector
            val prevVec = origin.toVector().add(getVector(previousTime))
            val direction = prevVec.subtract(origin.toVector().add(vector))
                .normalize() //get the direction vector of the previous vector to the current vector.
            if (!java.lang.Double.isNaN(direction.x) && !java.lang.Double.isNaN(direction.y) && !java.lang.Double.isNaN(
                    direction.z
                )
            ) {
                val dir = Location(
                    origin.world,
                    0.0,
                    0.0,
                    0.0
                ).setDirection(direction) //Create a location, so it calculates the angles for us :)
                //Calculate the rotation cos & sin
                val angleRad = Vector(Math.toRadians(dir.pitch.toDouble()), Math.toRadians(dir.yaw.toDouble()), 0.0)
                val xAxisCos = cos(angleRad.x) // getting the cos value for the pitch.
                val xAxisSin = sin(angleRad.x) // getting the sin value for the pitch.
                val yAxisCos = cos(-angleRad.y) // getting the cos value for the yaw.
                val yAxisSin = sin(-angleRad.y) // getting the sin value for the yaw.

                origin.add(vector)
                shape.drawVectors(time) { vec: Vector? ->
                    rotateAroundAxisX(
                        vec!!, xAxisCos, xAxisSin
                    )
                    rotateAroundAxisY(
                        vec,
                        yAxisCos,
                        yAxisSin
                    )
                    origin.add(vec)
                    spawnParticle(effect!!, origin, player)
                    origin.subtract(vec)
                }
                origin.subtract(vector)
                return
            }
        }
        origin.add(vector) //Add vector to origin
        shape.drawVectors(time) { vec: Vector? ->
            origin.add(vec!!)
            spawnParticle(effect!!, origin, player)
            origin.subtract(vec)
        }
        origin.subtract(vector)
    }

    private fun getVector(time: Double): Vector {
        val iterator = path.entries.iterator()
        var vector = Vector()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val activationTime = entry.key
            if (activationTime > time) {
                continue
            }
            vector = entry.value
        }
        return vector
    }

    override fun toString(): String {
        return "AnimatorVectorPath{" +
                "shape=" + shape +
                ", path=" + path +
                '}'
    }

    companion object {
        val KEY: Key = scaffoldingKey("vector_path")
    }
}
