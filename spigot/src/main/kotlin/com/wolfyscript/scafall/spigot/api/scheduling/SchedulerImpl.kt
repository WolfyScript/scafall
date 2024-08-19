package com.wolfyscript.scafall.spigot.api.scheduling

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.scheduling.Task
import com.wolfyscript.scafall.spigot.api.ScafallSpigot
import com.wolfyscript.scafall.spigot.api.into
import org.bukkit.Bukkit

internal class SchedulerImpl(private val scaffolding: ScafallSpigot) : Scheduler {

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