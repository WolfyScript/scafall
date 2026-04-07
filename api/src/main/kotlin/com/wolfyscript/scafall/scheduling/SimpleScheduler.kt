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
        10, Comparator.comparingLong { it.nextRunTicks }
    )
    private val tasks = ConcurrentHashMap<UUID, Task>()

    fun tick(server: MinecraftServer) {
        tickCount = server.tickCount

        while (pending.isNotEmpty() && pending.first().nextRunTicks <= tickCount) {
            val task = pending.remove()
            task.run()

            if (task.repeat.completed) {
                task.complete()
                tasks.remove(task.id)
            } else {
                task.repeat.tick()
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
            delay = Delay.amount(delay),
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
            delay = Delay.amount(delay),
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
            repeat = Repeat.forever(interval),
            delay = Delay.amount(delay),
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
            repeat = Repeat.forever(interval),
            delay = Delay.amount(delay),
            mod = plugin,
        )
        schedule(task)
        return task
    }

    internal fun schedule(task: ScafallTask) {
        tasks[task.id] = task
    }

}