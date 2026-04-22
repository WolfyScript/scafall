package com.wolfyscript.scafall.scheduling

sealed interface Timer {

    companion object {

        /**
         * Creates a [Timer] that runs forever until the server stops.
         *
         * @param tickInterval The amount of ticks to wait between task executions.
         */
        fun forever(tickInterval: Int): Forever {
            return TimerForever(tickInterval)
        }

        /**
         * Creates a [Timer] that runs the specified [amount] of times.
         *
         * @param tickInterval The amount of ticks to wait between task executions.
         */
        fun amount(amount: Int, tickInterval: Int): Amount {
            return TimerAmount(amount, tickInterval)
        }

    }

    /**
     * Whether the timer is completed or not
     */
    val completed: Boolean

    val nextRunTick: Int

    /**
     * Initiates the timer at the specified tick count
     */
    fun start(tickCount: Int)

    /**
     * Updates the timer at the current tick
     */
    fun update(currentTick: Int)


    /**
     * Runs a task only once
     */
    object Once : Timer {
        override var nextRunTick: Int = 0
        override var completed: Boolean = true

        override fun start(tickCount: Int) {
            nextRunTick = tickCount
        }

        override fun update(currentTick: Int) {}

    }

    /**
     * Runs a task only the specified [amount] of times with the specified [interval]
     */
    interface Amount : Timer {

        val amount: Int

        val interval: Int

    }

    /**
     * Runs a task forever (until the server stops) in the specified [interval]
     */
    interface Forever : Timer {

        val interval: Int

    }

}