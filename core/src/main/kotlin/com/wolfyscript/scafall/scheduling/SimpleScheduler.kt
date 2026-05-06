package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.ScafallProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 * A simple scheduler implementation that handles both synchronous and asynchronous tasks.
 *
 * This scheduler manages task execution with support for delayed execution and periodic scheduling.
 * It maintains separate queues for pending tasks and currently running tasks.
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

    /**
     * Executes a tick of the scheduler, processing tasks that are due to run.
     *
     * @param tickCount The current tick count to process tasks against
     */
    fun tick(tickCount: Int) {
        this.tickCount = tickCount

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

    override fun async(
        plugin: ModWrapper,
        delay: Delay,
        timer: Timer,
        task: suspend () -> Unit,
    ): Task {
        val task = AsyncTask(
            coroutineContext,
            fn = task,
            timer = timer,
            delay = delay,
            mod = plugin,
        )
        schedule(task)
        return task
    }

    override fun sync(
        plugin: ModWrapper,
        delay: Delay,
        timer: Timer,
        task: () -> Unit,
    ): Task {
        val task = SyncTask(
            fn = task,
            timer = timer,
            delay = delay,
            mod = plugin,
        )
        schedule(task)
        return task
    }

    override fun cancelAll(plugin: ModWrapper) {
        synchronized(runningTasks) {
            runningTasks.values.filter {
                it.mod == plugin
            }.forEach {
                it.cancel()
            }
        }
        val iterator = queuedTasks.values.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().mod == plugin) {
                iterator.remove()
            }
        }
    }

    /**
     * Adds a task to the queue for scheduling.
     *
     * @param task The task to schedule
     */
    internal fun schedule(task: ScafallTask) {
        queuedTasks[task.id] = task
    }

}