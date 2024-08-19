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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scafall.spigot.platform.math.MathUtil.getLineVectors
import com.wolfyscript.scafall.spigot.platform.math.Triangle
import org.bukkit.util.Vector
import java.util.function.Consumer
import kotlin.math.sqrt

class ShapeIcosahedron @JsonCreator constructor(
    @JsonProperty("radius") radius: Double,
    @JsonProperty("depth") depth: Int,
    @JsonProperty("particleIncrease") particleIncrease: Double
) :
    Shape(KEY) {
    private val radius: Double
    private val particleIncrease: Double
    private val depth: Int

    @JsonIgnore
    private val triangles: MutableList<Triangle> = ArrayList()

    /**
     * The cached vectors of the specified settings (So we don't recalculate them each run).
     */
    @JsonIgnore
    private val vectors: MutableList<Vector> = ArrayList()

    /**
     * Only used for Jackson deserialization.
     */
    internal constructor() : this(1.0, 0, 6.0)

    /**
     * Creates a new icosahedron with the specified radius, depth and particleIncrease.<br></br>
     * The depth specifies the amount of times each triangle will be subdivided into smaller triangles.<br></br>
     * The subdivision occurs recursively, so the amount of triangles will increase by the factor of 3 each depth.<br></br>
     * <pre>1 triangle -> 3 triangles -> 9 triangles -> ...</pre>
     * The vertices are calculated once on creation and cached, so they are not recalculated at runtime.
     *
     *
     * @param radius The radius of the icosahedron.
     * @param depth The amount of times the triangles are subdivided into smaller triangles.
     * @param particleIncrease The distance between the particle effects.
     */
    init {
        Preconditions.checkArgument(particleIncrease > 0, "Particle increase must be higher than 0!")
        Preconditions.checkArgument(depth >= 0, "Depth must be higher than or equal 0!")
        this.radius = radius
        this.depth = depth
        this.particleIncrease = particleIncrease

        calcIcosahedron(this.depth, this.radius)
    }

    override fun drawVectors(time: Double, drawVector: Consumer<Vector>) {
        vectors.forEach(Consumer { vector: Vector -> drawVector.accept(vector.clone()) })
    }

    private fun calcIcosahedron(depth: Int, radius: Double) {
        for (tindx in TINDX) {
            subdivide(
                V_DATA[tindx[0]],
                V_DATA[tindx[1]], V_DATA[tindx[2]], depth, radius
            )
        }
        // Cache vectors
        for ((point1, point2, point3) in triangles) {
            vectors.addAll(
                getLineVectors(
                    point1,
                    point2, particleIncrease
                )
            )
            vectors.addAll(
                getLineVectors(
                    point3,
                    point2, particleIncrease
                )
            )
        }
    }

    private fun calcTriangle(vA0: DoubleArray, vB1: DoubleArray, vC2: DoubleArray, radius: Double) {
        val triangle = Triangle(
            Vector(vA0[0], vA0[1], vA0[2]).multiply(radius),
            Vector(vB1[0], vB1[1], vB1[2]).multiply(radius),
            Vector(vC2[0], vC2[1], vC2[2]).multiply(radius)
        )
        triangles.add(triangle)
    }

    private fun subdivide(vA0: DoubleArray, vB1: DoubleArray, vC2: DoubleArray, depth: Int, radius: Double) {
        val vAB = DoubleArray(3)
        val vBC = DoubleArray(3)
        val vCA = DoubleArray(3)

        if (depth == 0) {
            calcTriangle(vA0, vB1, vC2, radius)
            return
        }

        var i = 0
        while (i < 3) {
            vAB[i] = (vA0[i] + vB1[i]) / 2
            vBC[i] = (vB1[i] + vC2[i]) / 2
            vCA[i] = (vC2[i] + vA0[i]) / 2
            i++
        }

        val modAB = mod(vAB)
        val modBC = mod(vBC)
        val modCA = mod(vCA)

        i = 0
        while (i < 3) {
            vAB[i] /= modAB
            vBC[i] /= modBC
            vCA[i] /= modCA
            i++
        }
        subdivide(vA0, vAB, vCA, depth - 1, radius)
        subdivide(vB1, vBC, vAB, depth - 1, radius)
        subdivide(vC2, vCA, vBC, depth - 1, radius)
        subdivide(vAB, vBC, vCA, depth - 1, radius)
    }

    companion object {
        val KEY: Key = scaffoldingKey("icosahedron")
        const val X: Double = 0.525731112119133606
        const val Z: Double = 0.850650808352039932

        /**
         * Vertices of the triangles
         */
        val V_DATA: Array<DoubleArray> = arrayOf(
            doubleArrayOf(-X, +0.0, +Z),
            doubleArrayOf(+X, +0.0, +Z),
            doubleArrayOf(-X, +0.0, -Z),
            doubleArrayOf(+X, +0.0, -Z),
            doubleArrayOf(+0.0, +Z, +X),
            doubleArrayOf(+0.0, +Z, -X),
            doubleArrayOf(+0.0, -Z, +X),
            doubleArrayOf(+0.0, -Z, -X),
            doubleArrayOf(+Z, +X, +0.0),
            doubleArrayOf(-Z, +X, +0.0),
            doubleArrayOf(+Z, -X, +0.0),
            doubleArrayOf(-Z, -X, +0.0)
        )
        val TINDX: Array<IntArray> = arrayOf(
            intArrayOf(0, 4, 1),
            intArrayOf(0, 9, 4),
            intArrayOf(9, 5, 4),
            intArrayOf(4, 5, 8),
            intArrayOf(4, 8, 1),
            intArrayOf(8, 10, 1),
            intArrayOf(8, 3, 10),
            intArrayOf(5, 3, 8),
            intArrayOf(5, 2, 3),
            intArrayOf(2, 7, 3),
            intArrayOf(7, 10, 3),
            intArrayOf(7, 6, 10),
            intArrayOf(7, 11, 6),
            intArrayOf(11, 0, 6),
            intArrayOf(0, 1, 6),
            intArrayOf(6, 1, 10),
            intArrayOf(9, 0, 11),
            intArrayOf(9, 11, 2),
            intArrayOf(9, 2, 5),
            intArrayOf(7, 2, 11)
        )

        private fun mod(v: DoubleArray): Double {
            return sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2])
        }
    }
}
