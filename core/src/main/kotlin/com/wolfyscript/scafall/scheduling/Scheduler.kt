package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper

/**
 * A scheduler for managing asynchronous and synchronous tasks within a mod environment.
 *
 * The scheduler provides mechanisms to execute tasks either asynchronously (without blocking
 * the main thread) or synchronously (on the main thread). Each task can be configured with
 * an initial delay and a timer that defines how often and for how long the task should run.
 */
interface Scheduler {

    /**
     * Schedules an asynchronous task to be executed.
     *
     * @param plugin The mod wrapper associated with the task
     * @param delay The initial delay before the task starts executing
     * @param timer The timer configuration specifying how often and for how long the task should run
     * @param task The suspend function to be executed asynchronously
     * @return A Task instance representing the scheduled task that can be cancelled
     */
    fun async(plugin: ModWrapper, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: suspend () -> Unit): Task

    /**
     * Schedules a synchronous task to be executed on the main thread.
     *
     * @param plugin The mod wrapper associated with the task
     * @param delay The initial delay before the task starts executing
     * @param timer The timer configuration specifying how often and for how long the task should run
     * @param task The function to be executed synchronously on the main thread
     * @return A Task instance representing the scheduled task that can be cancelled
     */
    fun sync(plugin: ModWrapper, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: () -> Unit): Task
    
}
