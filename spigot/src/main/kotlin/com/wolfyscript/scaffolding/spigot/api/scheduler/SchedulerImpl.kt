package com.wolfyscript.scaffolding.spigot.api.scheduler

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.scheduling.Scheduler
import com.wolfyscript.scaffolding.scheduling.Task
import com.wolfyscript.scaffolding.spigot.api.ScaffoldingSpigot
import com.wolfyscript.scaffolding.spigot.api.into
import org.bukkit.Bukkit

internal class SchedulerImpl(private val scaffolding: ScaffoldingSpigot) : Scheduler {

    override fun task(plugin: PluginWrapper): Task.Builder = TaskImpl.Builder(plugin.into())

    override fun syncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskLater(it.plugin, task, delay), it)
        }

    override fun asyncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskLaterAsynchronously(it.plugin, task, delay), it)
        }

    override fun syncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskTimer(it.plugin, task, delay, interval), it)
        }

    override fun asyncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskTimerAsynchronously(it.plugin, task, delay, interval), it)
        }
}