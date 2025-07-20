package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper

interface Scheduler {

    /**
     * Creates a new [Task Builder][Task.Builder] bound to the specified [ModWrapper]
     *
     * @param plugin The plugin that owns this task
     * @return The new Task Builder
     */
    fun task(plugin: ModWrapper): Task.Builder

    fun syncTask(plugin: ModWrapper, task: Runnable): Task {
        return syncTask(plugin, task, 0)
    }

    fun syncTask(plugin: ModWrapper, task: Runnable, delay: Long): Task

    fun asyncTask(plugin: ModWrapper, task: Runnable): Task {
        return asyncTask(plugin, task, 0)
    }

    fun asyncTask(plugin: ModWrapper, task: Runnable, delay: Long): Task

    fun syncTimerTask(plugin: ModWrapper, task: Runnable, delay: Long, interval: Long): Task

    fun asyncTimerTask(plugin: ModWrapper, task: Runnable, delay: Long, interval: Long): Task
}
