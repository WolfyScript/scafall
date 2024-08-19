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
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey

/**
 * Provides an increasing value by a fraction of PI.<br></br>
 * Formula: <br></br>
 * `t += Math.PI / fraction`
 */
class TimerPi @JvmOverloads constructor(
    fraction: Double = 1.0,
    stopValue: Double = Math.PI
) : Timer(KEY) {

    private var fraction: Double = fraction
        set(value) {
            Preconditions.checkArgument(value != 0.0, "Cannot divide by 0! Must be less or bigger than 0!")
            Preconditions.checkArgument(
                if (value > 0) (stopValue > 0) else (stopValue < 0),
                "Invalid fraction! A fraction of $value and a stop value of $stopValue will cause it to increase/decrease indefinitely!"
            )
            field = value
        }

    override var stopValue: Double = stopValue
        set(value) {
            Preconditions.checkArgument(
                if (fraction > 0) (value > 0) else (value < 0),
                "Invalid stop time! Value of $value will cause it to increase/decrease indefinitely!"
            )
            field = value
        }

    override fun createRunner(): Timer.Runner {
        return Runner()
    }

    private inner class Runner : Timer.Runner() {
        override fun increase(): Double {
            time += Math.PI / fraction
            return time
        }

        override fun shouldStop(): Boolean {
            if (fraction > 0) {
                return time > stopValue
            }
            return time < stopValue
        }
    }

    companion object {
        val KEY: Key = scaffoldingKey("pi_fraction")
    }
}
