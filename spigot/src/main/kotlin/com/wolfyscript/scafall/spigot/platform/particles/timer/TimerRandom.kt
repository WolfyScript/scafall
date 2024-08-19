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
package com.wolfyscript.scafall.spigot.platform.particles.timer

import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import java.util.*

/**
 * Provides a linear increasing value by a specified increment.<br></br>
 */
class TimerRandom @JvmOverloads constructor(
    val seed: Long = 1,
    private val multiplier: Double = 1.0,
    stopValue: Double = 1.0
) : Timer(KEY) {

    override var stopValue: Double
        get() = super.stopValue
        set(stopValue) {
            Preconditions.checkArgument(stopValue >= 0, "Random time supplier cannot have negative stop value!")
            super.stopValue = stopValue
        }

    init {
        this.stopValue = stopValue
    }

    override fun createRunner(): Timer.Runner {
        return Runner()
    }

    private inner class Runner : Timer.Runner() {
        private val random = if (seed != -1L) Random(seed) else Random()
        private var counter = 0

        override fun increase(): Double {
            time += random.nextDouble() * multiplier
            counter++
            return time
        }

        override fun shouldStop(): Boolean {
            return counter >= stopValue
        }
    }

    companion object {
        val KEY: Key = defaultKey("random")
    }
}
