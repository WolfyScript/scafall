package com.wolfyscript.scafall.scheduling

/**
 * Represents a timer that controls the execution schedule of tasks within the scheduling system.
 * Timers define when and how often a task should run, supporting both finite and infinite execution patterns.
 */
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

    /**
     * The tick when the next execution of the task should occur. This is used to queue the task for execution at a later time.
     */
    val nextRunTick: Int

    /**
     * Starts the timer with the specified tick count.
     *
     * @param tickCount The tick count to start the timer with. This is usually the current tick count of the server.
     */
    fun start(tickCount: Int)

    /**
     * Updates the timer state based on the current tick count.
     *
     * This method is responsible for managing the timing logic of a task,
     * including checking if the task should run and updating internal
     * state such as next execution time and completion status.
     *
     * @param currentTick The current tick count
     */
    fun update(currentTick: Int)


    /**
     * A [Timer] that executes a task exactly once.
     *
     * The timer starts at a specified tick count and immediately completes.
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
     * A [Timer] that executes a task a specified number of times.
     *
     * This timer executes a task at regular intervals defined by the [interval] property.
     */
    interface Amount : Timer {

        /**
         * The number of times the task should be executed. The timer will complete after this many executions.
         */
        val amount: Int

        /**
         * The time delay between consecutive task executions in ticks.
         */
        val interval: Int

    }

    /**
     * A [Timer] that executes a task that runs forever until the server stops.
     *
     * This timer executes a task at regular intervals defined by the [interval] property.
     * The execution continues indefinitely without completing.
     */
    interface Forever : Timer {

        /**
         * The time delay between consecutive task executions in ticks.
         */
        val interval: Int

    }

}