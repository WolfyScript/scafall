package com.wolfyscript.scafall.scheduler

/**
 * A scheduler for managing asynchronous and synchronous timed and reoccurring tasks.
 *
 * The scheduler provides mechanisms to execute tasks either asynchronously (without blocking
 * the main thread) or synchronously (on the main thread). Each task can be configured with
 * an initial delay and a timer that defines how often and for how long the task should run.
 */
interface Scheduler {

    /**
     * Schedules an asynchronous task to be executed.
     *
     * @param owner The owner associated with the task
     * @param delay The initial delay before the task starts executing
     * @param timer The timer configuration specifying how often and for how long the task should run
     * @param task The suspend function to be executed asynchronously
     * @return A Task instance representing the scheduled task that can be cancelled
     */
    fun async(owner: TaskOwner, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: suspend () -> Unit): Task

    /**
     * Schedules a synchronous task to be executed on the main thread.
     *
     * @param owner The owner associated with the task
     * @param delay The initial delay before the task starts executing
     * @param timer The timer configuration specifying how often and for how long the task should run
     * @param task The function to be executed synchronously on the main thread
     * @return A Task instance representing the scheduled task that can be cancelled
     */
    fun sync(owner: TaskOwner, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: () -> Unit): Task

    /**
     * Cancels all asynchronous and synchronous tasks that are associated with the given [TaskOwner].
     *
     * This method provides a way to stop all tasks that were scheduled for a specific owner. This is useful
     * in scenarios where the owner is no longer available or needs to be cleaned up.
     *
     * @param owner The [TaskOwner] for which to cancel all tasks.
     */
    fun cancelAll(owner: TaskOwner)
    
}
