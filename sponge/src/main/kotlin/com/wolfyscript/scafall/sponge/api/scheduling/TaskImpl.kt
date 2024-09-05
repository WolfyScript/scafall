package com.wolfyscript.scafall.sponge.api.scheduling

import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.function.ReceiverConsumer
import com.wolfyscript.scafall.scheduling.Task
import com.wolfyscript.scafall.sponge.api.SpongePluginWrapper
import org.spongepowered.api.Sponge
import org.spongepowered.api.scheduler.ScheduledTask
import org.spongepowered.api.util.Ticks
import java.util.function.Consumer

internal class TaskImpl(private val plugin: PluginWrapper, private val scheduledTask: ScheduledTask) : Task {

    override fun cancel() {
        scheduledTask.cancel()
    }

    override fun plugin(): PluginWrapper = plugin

    internal class BuilderImpl(private val plugin: PluginWrapper) : Task.Builder {
        private var async = false
        private val builder: org.spongepowered.api.scheduler.Task.Builder =
            org.spongepowered.api.scheduler.Task.builder()
        private var executor: ReceiverConsumer<Task>? = null

        init {
            builder.plugin((plugin as SpongePluginWrapper).plugin)
        }

        override fun async(): Task.Builder {
            this.async = true
            return this
        }

        override fun delay(ticks: Long): Task.Builder {
            builder.delay(Ticks.of(ticks))
            return this
        }

        override fun interval(ticks: Long): Task.Builder {
            builder.interval(Ticks.of(ticks))
            return this
        }

        override fun execute(runnable: Runnable): Task.Builder {
            builder.execute(runnable)
            return this
        }

        override fun execute(executor: ReceiverConsumer<Task>): Task.Builder {
            this.executor = executor
            return this
        }

        override fun build(): Task {
            if (executor != null) {
                val scheduledTaskConsumerWrapper = ScheduledTaskConsumerWrapper(plugin, executor!!)
                builder.execute(scheduledTaskConsumerWrapper)
            }
            val spongeTask = builder.build()
            val scheduledTask = if (async) {
                Sponge.asyncScheduler().submit(spongeTask)
            } else {
                Sponge.server().scheduler().submit(spongeTask)
            }
            return TaskImpl(plugin, scheduledTask)
        }
    }

    private class ScheduledTaskConsumerWrapper(private val plugin: PluginWrapper,
                                               private val executor: ReceiverConsumer<Task>
    ) :
        Consumer<ScheduledTask> {
        var task: Task? = null

        override fun accept(scheduledTask: ScheduledTask) {
            if (task == null) {
                task = TaskImpl(plugin, scheduledTask)
            }
            with(executor) {
                task?.consume()
            }
        }
    }
}
