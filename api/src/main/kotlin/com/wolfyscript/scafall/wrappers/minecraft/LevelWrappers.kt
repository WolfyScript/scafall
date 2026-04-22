package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.wrappers.ScafallLevel
import net.minecraft.world.level.Level

fun Level.wrap(): ScafallLevel = ScafallLevelImpl(this)

fun ScafallLevel.unwrap(): Level = (this as ScafallLevelImpl).level

internal class ScafallLevelImpl(val level: Level) : ScafallLevel
