package com.wolfyscript.scafall.sponge.api.scheduling

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.scheduling.Task

class SchedulerImpl : Scheduler {

    override fun task(plugin: ModWrapper): Task.Builder = TaskImpl.BuilderImpl(plugin)

    override fun syncTask(plugin: ModWrapper, task: Runnable, delay: Long): Task = task(plugin).execute(task).delay(delay).build()

    override fun asyncTask(plugin: ModWrapper, task: Runnable, delay: Long): Task = task(plugin).async().execute(task).delay(delay).build()

    override fun syncTimerTask(plugin: ModWrapper, task: Runnable, delay: Long, interval: Long): Task = task(plugin).execute(task).delay(delay).interval(interval).build()

    override fun asyncTimerTask(plugin: ModWrapper, task: Runnable, delay: Long, interval: Long): Task = task(plugin).async().execute(task).delay(delay).interval(interval).build()
}