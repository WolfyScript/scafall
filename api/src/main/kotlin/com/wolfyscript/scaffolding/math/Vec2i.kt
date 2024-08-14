package com.wolfyscript.scaffolding.math

import com.google.common.base.Objects
import com.google.common.primitives.Doubles
import kotlin.math.acos
import kotlin.math.sqrt

class Vec2i(
    x: Int,
    y: Int
) {
    var x: Int = x
        private set
    var y: Int = y
        private set
    /**
     * Adds a vector to this one
     *
     * @param vec The other vector
     * @return the same vector
     */
    fun add(vec: Vec2i): Vec2i {
        x += vec.x
        y += vec.y
        return this
    }

    /**
     * Subtracts a vector from this one.
     *
     * @param vec The other vector
     * @return the same vector
     */
    fun subtract(vec: Vec2i): Vec2i {
        x -= vec.x
        y -= vec.y
        return this
    }

    /**
     * Multiplies the vector by another.
     *
     * @param vec The other vector
     * @return the same vector
     */
    fun multiply(vec: Vec2i): Vec2i {
        x *= vec.x
        y *= vec.y
        return this
    }

    /**
     * Divides the vector by another.
     *
     * @param vec The other vector
     * @return the same vector
     */
    fun divide(vec: Vec2i): Vec2i {
        x /= vec.x
        y /= vec.y
        return this
    }

    /**
     * Copies another vector
     *
     * @param vec The other vector
     * @return the same vector
     */
    fun copy(vec: Vec2i): Vec2i {
        x = vec.x
        y = vec.y
        return this
    }

    /**
     * Gets the magnitude of the vector, defined as sqrt(x^2+y^2). The
     * value of this method is not cached and uses a costly square-root
     * function, so do not repeatedly call this method to get the vector's
     * magnitude. NaN will be returned if the inner result of the sqrt()
     * function overflows, which will be caused if the length is too long.
     *
     * @return the magnitude
     */
    fun length(): Double {
        return sqrt(lengthSquared().toDouble())
    }

    /**
     * Gets the magnitude of the vector squared.
     *
     * @return the magnitude
     */
    fun lengthSquared(): Int {
        return x * x + y * y
    }

    /**
     * Calculates the distance between this vector and another. The value of this
     * method is not cached and uses a costly square-root function, so do not
     * repeatedly call this method to get the vector's magnitude. NaN will be
     * returned if the inner result of the sqrt() function overflows, which
     * will be caused if the distance is too long.
     *
     * @param other The other vector
     * @return the distance
     */
    fun distance(other: Vec2i): Double {
        return sqrt(distanceSquared(other))
    }

    /**
     * Get the squared distance between this vector and another.
     *
     * @param other The other vector
     * @return the distance
     */
    fun distanceSquared(other: Vec2i): Double {
        val dx = other.x - x
        val dy = other.y - y
        return (dx * dx + dy * dy).toDouble()
    }

    /**
     * Gets the angle between this vector and another in radians.
     *
     * @param other The other vector
     * @return angle in radians
     */
    fun angle(other: Vec2i): Float {
        val dot = Doubles.constrainToRange(dot(other) / (length() * other.length()), -1.0, 1.0)
        return acos(dot) as Float
    }

    /**
     * Sets this vector to the midpoint between this vector and another.
     *
     * @param other The other vector
     * @return this same vector (now a midpoint)
     */
    fun midpoint(other: Vec2i): Vec2i {
        x = (x + other.x) / 2
        y = (y + other.y) / 2
        return this
    }

    /**
     * Gets a new midpoint vector between this vector and another.
     *
     * @param other The other vector
     * @return a new midpoint vector
     */
    fun getMidpoint(other: Vec2i): Vec2i {
        val x = (this.x + other.x) / 2
        val y = (this.y + other.y) / 2
        return Vec2i(x, y)
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    fun multiply(m: Int): Vec2i {
        x *= m
        y *= m
        return this
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    fun multiply(m: Double): Vec2i {
        x = (x * m).toInt()
        y = (y * m).toInt()
        return this
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    fun multiply(m: Float): Vec2i {
        x = (x * m).toInt()
        y = (y * m).toInt()
        return this
    }

    /**
     * Calculates the dot product of this vector with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     *
     * @param other The other vector
     * @return dot product
     */
    fun dot(other: Vec2i): Double {
        return (x * other.x + y * other.y).toDouble()
    }

    /**
     * Converts this vector to a unit vector (a vector with length of 1).
     *
     * @return the same vector
     */
    fun normalize(): Vec2i {
        val length = length()
        x = (x / length).toInt()
        y = (y / length).toInt()
        return this
    }

    /**
     * Zero this vector's components.
     *
     * @return the same vector
     */
    fun zero(): Vec2i {
        x = 0
        y = 0
        return this
    }

    /**
     * Converts each component of value `-0.0` to `0.0`.
     *
     * @return This vector.
     */
    fun normalizeZeros(): Vec2i {
        if (x.toDouble() == -0.0) x = 0
        if (y.toDouble() == -0.0) y = 0
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val vec2i = o as Vec2i
        return x == vec2i.x && y == vec2i.y
    }

    override fun hashCode(): Int {
        return Objects.hashCode(x, y)
    }
}
