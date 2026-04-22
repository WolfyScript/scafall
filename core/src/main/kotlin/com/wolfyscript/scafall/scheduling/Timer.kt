package com.wolfyscript.scafall.scheduling

sealed interface Timer {

    companion object {

        fun forever(tickInterval: Int): Forever {
            return TimerForever(tickInterval)
        }

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
     *
     */
    object Once : Timer {
        override var nextRunTick: Int = 0
        override var completed: Boolean = true

        override fun start(tickCount: Int) {
            nextRunTick = tickCount
        }

        override fun update(currentTick: Int) {}

    }

    interface Amount : Timer {

        val amount: Int

        val interval: Int

    }

    interface Forever : Timer {

        val interval: Int

    }

}