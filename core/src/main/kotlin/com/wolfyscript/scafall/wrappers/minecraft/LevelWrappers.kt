package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.wrappers.world.level.ScafallLevel
import com.wolfyscript.scafall.wrappers.world.level.ScafallLevelImpl
import net.minecraft.world.level.Level

/**
 * Wraps the [net.minecraft.world.level.Level] instance in a [ScafallLevel].
 *
 * @return A [ScafallLevel] instance that wraps the [Level].
 */
fun Level.wrap(): ScafallLevel = ScafallLevelImpl(this)

/**
 * Unwraps the [ScafallLevel] instance back into the original [net.minecraft.world.level.Level].
 *
 * @return The original [Level] instance wrapped by the [ScafallLevel].
 */
fun ScafallLevel.unwrap(): Level = (this as ScafallLevelImpl).level
