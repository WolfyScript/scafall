package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper

interface Task {
    //    long delay();
    //    long interval();

    fun cancel()

    fun plugin(): ModWrapper

    interface Builder {
        fun async(): Builder

        fun delay(ticks: Long): Builder

        fun interval(ticks: Long): Builder

        fun execute(runnable: Runnable): Builder

        fun execute(executor: Task.() -> Unit): Builder

        fun build(): Task
    }
}
