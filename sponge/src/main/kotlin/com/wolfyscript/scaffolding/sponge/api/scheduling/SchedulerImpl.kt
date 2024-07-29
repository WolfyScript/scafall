package com.wolfyscript.scaffolding.sponge.api.scheduling

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.scheduling.Scheduler
import com.wolfyscript.scaffolding.scheduling.Task

class SchedulerImpl : Scheduler {

    override fun task(plugin: PluginWrapper): Task.Builder = TaskImpl.BuilderImpl(plugin)

    override fun syncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task = task(plugin).execute(task).delay(delay).build()

    override fun asyncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task = task(plugin).async().execute(task).delay(delay).build()

    override fun syncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task = task(plugin).execute(task).delay(delay).interval(interval).build()

    override fun asyncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task = task(plugin).async().execute(task).delay(delay).interval(interval).build()
}