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
package com.wolfyscript.scafall.spigot.platform.math

import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object MathUtil {

    @JvmStatic
    fun rotateAroundAxisX(v: Vector, cos: Double, sin: Double): Vector {
        val y = v.y * cos - v.z * sin
        val z = v.y * sin + v.z * cos
        return v.setY(y).setZ(z)
    }

    @JvmStatic
    fun rotateAroundAxisY(v: Vector, cos: Double, sin: Double): Vector {
        val x = v.x * cos + v.z * sin
        val z = v.x * -sin + v.z * cos
        return v.setX(x).setZ(z)
    }

    @JvmStatic
    fun rotateAroundAxisZ(v: Vector, cos: Double, sin: Double): Vector {
        val x = v.x * cos - v.y * sin
        val y = v.x * sin + v.y * cos
        return v.setX(x).setY(y)
    }

    @JvmStatic
    fun degreeToRadiansVector(angleVec: Vector): Vector {
        angleVec.setX(Math.toRadians(angleVec.x))
        angleVec.setY(Math.toRadians(angleVec.y))
        angleVec.setZ(Math.toRadians(angleVec.z))
        return angleVec
    }

    @JvmStatic
    fun rotate(vec: Vector, rotationVec: Vector): Vector {
        return rotate(vec, getRotationAngles(rotationVec))
    }

    @JvmStatic
    fun rotate(vec: Vector, angles: Array<DoubleArray>): Vector {
        rotateAroundAxisX(vec, angles[0][0], angles[0][1])
        rotateAroundAxisY(vec, angles[1][0], angles[1][1])
        rotateAroundAxisZ(vec, angles[2][0], angles[2][1])
        return vec
    }

    @JvmStatic
    fun getRotationAngles(rotationVec: Vector): Array<DoubleArray> {
        return arrayOf(
            doubleArrayOf(cos(rotationVec.x), sin(rotationVec.x)),
            doubleArrayOf(cos(-rotationVec.y), sin(-rotationVec.y)),
            doubleArrayOf(cos(rotationVec.z), sin(rotationVec.z))
        )
    }

    @JvmStatic
    fun getLineVectors(start: Vector, end: Vector, increments: Double): List<Vector> {
        val vectors: MutableList<Vector> = LinkedList()
        val dirVec = end.clone().subtract(start).normalize()
        val distance = end.distance(start)
        var i = increments
        while (i <= distance) {
            if (i == 0.0) break
            dirVec.multiply(i) // multiply
            start.add(dirVec) // add
            vectors.add(start.clone())
            start.subtract(dirVec) // subtract
            dirVec.normalize() // normalize
            i += increments
        }
        return vectors
    }
}
