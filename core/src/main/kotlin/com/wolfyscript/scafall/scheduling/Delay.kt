package com.wolfyscript.scafall.scheduling

/**
 * The initial delay of a [Task] after queuing it.
 */
interface Delay {

    companion object {

        /**
         * Delays the task by the given amount of ticks
         */
        fun amount(ticks: Int) : Amount {
            return DelayAmount(ticks)
        }

    }

    val ticks: Int

    /**
     * Runs a task on the next possible tick without further delay.
     */
    object Instant : Delay {
        override val ticks: Int = 0
    }

    /**
     * Runs a task after the specified amount of ticks have passed since queuing the task.
     */
    interface Amount : Delay

}