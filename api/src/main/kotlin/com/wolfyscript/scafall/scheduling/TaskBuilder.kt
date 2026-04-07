package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

internal class TaskBuilder(
    val scheduler: SimpleScheduler,
    val parentCoroutineContext: CoroutineContext = Dispatchers.Default,
    val mod: ModWrapper
) : Task.Builder {

    private var async = false
    private var delay: Delay = Delay.Instant
    private var timer: Timer = Timer.Once
    private var fn: Task.() -> Unit = {}

    override fun async(): Task.Builder = apply {
        async = true
    }

    override fun delay(ticks: Long): Task.Builder = apply {
        delay = if (ticks <= 0) {
            Delay.Instant
        } else {
            Delay.amount(ticks.toInt())
        }
    }

    override fun interval(ticks: Long): Task.Builder = apply {
        timer = if (ticks <= 0) {
            Timer.forever(ticks.toInt())
        } else {
            Timer.Once
        }
    }

    override fun execute(runnable: Runnable): Task.Builder = apply {
        fn = { runnable.run() }
    }

    override fun execute(executor: Task.() -> Unit): Task.Builder = apply {
        fn = executor
    }

    override fun build(): Task {
        val task = if (async) {
            AsyncTask(
                parentCoroutineContext,
                fn = fn,
                delay = delay,
                timer = timer,
                mod = mod
            )
        } else {
            SyncTask(
                mod = mod,
                fn = fn,
                delay = delay,
                timer = timer,
            )
        }
        scheduler.schedule(task)
        return task
    }

}