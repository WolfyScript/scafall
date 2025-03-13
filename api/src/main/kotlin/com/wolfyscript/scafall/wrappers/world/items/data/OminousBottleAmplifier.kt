package com.wolfyscript.scafall.wrappers.world.items.data

import org.checkerframework.common.value.qual.IntRange

data class OminousBottleAmplifier(val amplifier: @IntRange(from = 0, to = 4) Int)