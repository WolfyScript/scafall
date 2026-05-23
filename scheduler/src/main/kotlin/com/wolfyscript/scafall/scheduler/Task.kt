package com.wolfyscript.scafall.scheduler

import java.util.UUID

/**
 * Represents a task that can be scheduled and executed within the [Scheduler].
 * Tasks are associated with an owner and can be cancelled before/during execution.
 *
 * Tasks are executed according to their scheduling configuration, which includes
 * initial delay and timer settings that determine when and how often they run.
 */
interface Task {

    /**
     * The [com.wolfyscript.scafall.ModWrapper] associated with the task.
     */
    val owner: TaskOwner

    /**
     * The [Timer] associated with the task, defining when and how often the task should execute.
     *
     * It controls the scheduling behaviour of the task, determining whether it runs once,
     * a specific number of times, or continuously until the server stops.
     */
    val timer: Timer

    /**
     * The [Delay] associated with the task, defining a delay before task execution.
     */
    val delay: Delay

    /**
     * The unique identifier for the task.
     */
    val id: UUID

    /**
     * Whether the task is synchronous and runs on the main thread.
     */
    val sync: Boolean

    /**
     * Whether the task has completed execution.
     *
     * This is true when the task has finished executing and will not run again.
     * Which is the case when the [timer] has completed, or when the task has been cancelled.
     */
    val isCompleted: Boolean

    /**
     * Cancels the task.
     * This prevents the task from executing in the future.
     * If the task is currently running, it will finish execution.
     */
    fun cancel()

}
