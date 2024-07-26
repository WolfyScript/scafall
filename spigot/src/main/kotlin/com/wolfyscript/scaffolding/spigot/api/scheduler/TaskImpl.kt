package com.wolfyscript.scaffolding.spigot.api.scheduler

import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.function.ReceiverConsumer
import com.wolfyscript.scaffolding.scheduling.Task
import com.wolfyscript.scaffolding.spigot.api.ScaffoldingSpigot
import com.wolfyscript.scaffolding.spigot.api.SpigotPluginWrapper
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

internal class TaskImpl(private val task: BukkitTask, private val plugin: PluginWrapper) : Task {

    override fun cancel() = task.cancel()

    override fun plugin(): PluginWrapper = plugin

    internal class Builder(private val plugin: SpigotPluginWrapper) : Task.Builder {

        private var async: Boolean = false
        private var delay: Long = 0
        private var interval: Long = -1
        private var taskRunnable: Runnable? = null
        private var executor: ReceiverConsumer<Task>? = null

        override fun async(): Task.Builder = apply {
            async = true
        }

        override fun delay(ticks: Long): Task.Builder = apply {
            delay = ticks
        }

        override fun interval(ticks: Long): Task.Builder = apply {
            interval = ticks
        }

        override fun execute(runnable: Runnable): Task.Builder = apply {
            taskRunnable = runnable
        }

        override fun execute(executor: ReceiverConsumer<Task>): Task.Builder = apply {
            this.executor = executor
        }

        override fun build(): Task {
            val bukkitScheduler = Bukkit.getScheduler()

            // An interval of -1 causes the following tasks to act as a non-timer task
            executor?.run {
                // If we use an executor we cannot use the Bukkit scheduler methods, that also use executors, because they do not return the task!
                // Instead, we use our own Runnable wrapper that provides the task to the executor.
                val selfSupplyRunnable = SelfSupplyRunnable(bukkitScheduler, this) // TODO: Find a different way to pass the task to the runnable?!
                val task: Task = TaskImpl(constructTaskFromSettings(bukkitScheduler, selfSupplyRunnable, plugin.plugin), plugin)
                selfSupplyRunnable.task = task
                return task
            }

            return taskRunnable?.let { TaskImpl(constructTaskFromSettings(bukkitScheduler, it, plugin.plugin), plugin) }
                ?: throw IllegalStateException("Cannot schedule task without a Runnable")

        }

        private fun constructTaskFromSettings(bukkitScheduler: BukkitScheduler, runnable: Runnable, plugin: Plugin): BukkitTask {
            if (async) {
                return bukkitScheduler.runTaskTimerAsynchronously(plugin, runnable, delay, interval)
            }
            return bukkitScheduler.runTaskTimer(plugin, runnable, delay, interval)
        }

    }

    internal class SelfSupplyRunnable(bukkitScheduler: BukkitScheduler, private val executor: ReceiverConsumer<Task>) : Runnable {

        internal lateinit var task: Task

        override fun run() {
            with(executor) {
                task.consume()
            }
        }

    }

}