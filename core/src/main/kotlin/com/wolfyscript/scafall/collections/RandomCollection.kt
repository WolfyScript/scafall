package com.wolfyscript.scafall.collections

import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.function.BiConsumer
import java.util.stream.Collector

/**
 * Implementation of a weighted random collection.<br></br>
 * The chance of the values in the collection are based on their individual weight and total weight.<br></br>
 * <pre>valueChance = valueWeight / totalWeight</pre>
 *
 * @param <E> The type of the values
</E> */
class RandomCollection<E> : TreeMap<Double, E> {
    private var total = 0.0
    private val random: Random?

    constructor() {
        this.random = null
    }

    /**
     * When using this constructor the specified Random will be used whenever an item is selected.
     *
     * @param random The random to use when selecting an item.
     */
    constructor(random: Random) {
        this.random = random
    }

    /**
     * Adds a new value with the specified weight to the collection.<br></br>
     *
     * @param weight The weight of the value.
     * @param result The value to store.
     * @return This collection to allow for chaining.
     */
    fun add(weight: Double, result: E): RandomCollection<E> {
        if (weight <= 0) return this
        total += weight
        put(total, result)
        return this
    }

    /**
     * Gets the next value in the map depending on the weight.
     *
     * @return The next random value or null if none can be found.
     */
    @JvmOverloads
    fun next(random: Random = this.random ?: ThreadLocalRandom.current()): E? {
        val value = random.nextDouble() * total
        val entry = ceilingEntry(value)
        return entry?.value
    }

    fun addAll(randomCollection: RandomCollection<E>): RandomCollection<E> {
        putAll(randomCollection)
        return this
    }

    companion object {

        fun <T> createCollector(accumulator: BiConsumer<RandomCollection<T>, T>): Collector<T, RandomCollection<T>, RandomCollection<T>> =
            Collector.of({ RandomCollection() }, accumulator, { existing, other -> existing.addAll(other) })

    }
}
