package com.wolfyscript.scafall.spigot.api.scheduling

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.scheduling.Task
import com.wolfyscript.scafall.spigot.api.into
import org.bukkit.Bukkit

internal class SchedulerImpl() : Scheduler {

    override fun task(plugin: ModWrapper): Task.Builder = TaskImpl.Builder(plugin.into())

    override fun syncTask(plugin: ModWrapper, task: Runnable, delay: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskLater(it.plugin, task, delay), it)
        }

    override fun asyncTask(plugin: ModWrapper, task: Runnable, delay: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskLaterAsynchronously(it.plugin, task, delay), it)
        }

    override fun syncTimerTask(plugin: ModWrapper, task: Runnable, delay: Long, interval: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskTimer(it.plugin, task, delay, interval), it)
        }

    override fun asyncTimerTask(plugin: ModWrapper, task: Runnable, delay: Long, interval: Long): Task =
        plugin.into().let {
            TaskImpl(Bukkit.getScheduler().runTaskTimerAsynchronously(it.plugin, task, delay, interval), it)
        }
}