package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.PluginWrapper

interface Scheduler {

    /**
     * Creates a new [Task Builder][Task.Builder] bound to the specified [PluginWrapper]
     *
     * @param plugin The plugin that owns this task
     * @return The new Task Builder
     */
    fun task(plugin: PluginWrapper): Task.Builder

    fun syncTask(plugin: PluginWrapper, task: Runnable): Task {
        return syncTask(plugin, task, 0)
    }

    fun syncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task

    fun asyncTask(plugin: PluginWrapper, task: Runnable): Task {
        return asyncTask(plugin, task, 0)
    }

    fun asyncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task

    fun syncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task

    fun asyncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task
}
