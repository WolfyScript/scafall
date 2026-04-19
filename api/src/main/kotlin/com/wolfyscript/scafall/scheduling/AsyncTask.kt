package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

internal class AsyncTask(
    parentCoroutineContext: CoroutineContext,
    override val id: UUID = UUID.randomUUID(),
    val fn: suspend () -> Unit,
    override val timer: Timer = Timer.Once,
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
        launch(CoroutineName("$id")) {
            fn()
        }
    }

    override fun cancel() {
        taskJob.cancel()
    }

    override fun plugin(): ModWrapper = mod

}