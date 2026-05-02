package com.wolfyscript.scafall.scheduling

/**
 * The initial delay of a [Task] after queuing it.
 */
interface Delay {

    companion object {

        /**
         * Creates a delay amount that represents a specific number of ticks.
         *
         * @param ticks The number of ticks to delay execution
         * @return A delay amount representing the specified tick count
         */
        fun amount(ticks: Int) : Amount {
            return DelayAmount(ticks)
        }

    }

    val ticks: Int

    /**
     * Represents an instant delay value that indicates no delay should be applied,
     * effectively executing the task immediately upon scheduling.
     */
    object Instant : Delay {
        override val ticks: Int = 0
    }

    /**
     * Runs a task after the specified amount of ticks have passed since queuing the task.
     */
    interface Amount : Delay

}