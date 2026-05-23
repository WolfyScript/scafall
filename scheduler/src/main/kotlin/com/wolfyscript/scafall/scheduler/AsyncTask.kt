package com.wolfyscript.scafall.scheduler

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * An asynchronous task that runs in the background without blocking the main thread.
 *
 * This task executes a suspendable function in the background and can be configured
 * with timers and delays. It runs independently of the main thread and supports
 * coroutine cancellation.
 *
 * @param parentCoroutineContext The coroutine context from which this task inherits
 * @property id A unique identifier for this task
 * @property fn The suspendable function to execute
 * @property timer The timer configuration for task execution scheduling
 * @property delay The initial delay before task execution begins
 * @property owner The mod wrapper associated with this task
 */
internal class AsyncTask(
    parentCoroutineContext: CoroutineContext,
    override val id: UUID = UUID.randomUUID(),
    val fn: suspend () -> Unit,
    override val timer: Timer = Timer.Once,
    override val delay: Delay = Delay.Instant,
    override val owner: TaskOwner,
) : ScafallTask, CoroutineScope {

    override var nextRunTicks: Long = 0
    internal val taskJob = Job(parentCoroutineContext[Job])

    override val coroutineContext: CoroutineContext = parentCoroutineContext + taskJob

    override val sync: Boolean = false
    override val isCompleted: Boolean
        get() = taskJob.isCompleted

    override fun run() {
        if (taskJob.isCompleted) {
            return
        }
        launch(CoroutineName("$id")) {
            fn()
        }
    }

    override fun cancel() {
        taskJob.cancel()
    }

}