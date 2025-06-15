package com.wolfyscript.scafall.eval.value_provider

import java.util.*

interface RandomProvider<V> : ValueProvider<V> {
    fun sample(random: Random): V
}
