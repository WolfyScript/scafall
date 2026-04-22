package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper

interface Scheduler {

    /**
     * Runs an async task that won't block the main thread.
     *
     * The task is suspendable and can make use of kotlin coroutine features.
     *
     * @param delay The initial delay for the task
     * @param timer The timer specifying the lifetime (and interval) of the task
     * @param task The task to run
     */
    fun async(plugin: ModWrapper, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: suspend () -> Unit): Task

    /**
     * Runs a task on the main thread.
     *
     * @param delay The initial delay for the task
     * @param timer The timer specifying the lifetime (and interval) of the task
     * @param task The task to run
     */
    fun sync(plugin: ModWrapper, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: () -> Unit): Task
    
}
