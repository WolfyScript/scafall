package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.ScafallProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.minecraft.server.MinecraftServer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 */
class SimpleScheduler : Scheduler, CoroutineScope {

    private val asyncCoroutineDispatcher = Dispatchers.Default
    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { context, throwable ->
        ScafallProvider.get().logger.error("[Scafall][Scheduler] An error occurred in async task ${context[CoroutineName]?.name ?: ""}", throwable)
    }
    override val coroutineContext: CoroutineContext = asyncCoroutineDispatcher + exceptionHandler

    @Volatile
    private var tickCount = 0

    private val pending: PriorityQueue<ScafallTask> = PriorityQueue(
        10, Comparator.comparingInt { it.timer.nextRunTick }
    )
    private val runningTasks = HashMap<UUID, ScafallTask>()
    private val queuedTasks = ConcurrentHashMap<UUID, ScafallTask>()

    fun tick(server: MinecraftServer) {
        tickCount = server.tickCount

        // New queued tasks from previous tick not yet in the pending priority queue
        for (task in queuedTasks.values) {
            task.timer.start(tickCount + task.delay.ticks)
            pending.add(task)
        }
        queuedTasks.clear()

        // Run the tasks that are scheduled for this tick (or previous ticks in case ticks were skipped)
        while (pending.isNotEmpty() && pending.first().timer.nextRunTick <= tickCount) {
            val task = pending.remove()
            runningTasks[task.id] = task
            task.run()

            if (task.timer.completed) {
                runningTasks.remove(task.id)
            } else {
                task.timer.update(tickCount)
                pending.add(task)
            }
        }
    }

    override fun task(plugin: ModWrapper): Task.Builder {
        return TaskBuilder(this, coroutineContext, plugin)
    }

    override fun syncTask(
        plugin: ModWrapper,
        task: Runnable,
        delay: Long,
    ): Task {
        val task = SyncTask(
            fn = { task.run() },
            delay = Delay.amount(delay.toInt()),
            mod = plugin,
        )
        schedule(task)
        return task
    }

    override fun asyncTask(
        plugin: ModWrapper,
        task: Runnable,
        delay: Long,
    ): Task {
        val task = AsyncTask(
            coroutineContext,
            fn = { task.run() },
            delay = Delay.amount(delay.toInt()),
            mod = plugin,
        )
        schedule(task)
        return task
    }

    override fun syncTimerTask(
        plugin: ModWrapper,
        task: Runnable,
        delay: Long,
        interval: Long,
    ): Task {
        val task = SyncTask(
            fn = { task.run() },
            timer = Timer.forever(interval.toInt()),
            delay = Delay.amount(delay.toInt()),
            mod = plugin,
        )
        schedule(task)
        return task
    }

    override fun asyncTimerTask(
        plugin: ModWrapper,
        task: Runnable,
        delay: Long,
        interval: Long,
    ): Task {
        val task = AsyncTask(
            coroutineContext,
            fn = { task.run() },
            timer = Timer.forever(interval.toInt()),
            delay = Delay.amount(delay.toInt()),
            mod = plugin,
        )
        schedule(task)
        return task
    }

    internal fun schedule(task: ScafallTask) {
        queuedTasks[task.id] = task
    }

}