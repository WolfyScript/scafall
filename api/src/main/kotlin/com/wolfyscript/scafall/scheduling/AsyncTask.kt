package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

internal class AsyncTask(
    parentCoroutineContext: CoroutineContext,
    override val id: UUID = UUID.randomUUID(),
    override val fn: Task.() -> Unit,
    override val repeat: Repeat = Repeat.Never,
    override val delay: Delay = Delay.Instant,
    val mod: ModWrapper,
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
        launch {
            this@AsyncTask.fn()
        }
    }

    override fun complete() {
        taskJob.complete()
    }

    override fun cancel() {
        taskJob.cancel()
    }

    override fun plugin(): ModWrapper = mod

}