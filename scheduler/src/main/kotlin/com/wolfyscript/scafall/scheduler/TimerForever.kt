package com.wolfyscript.scafall.scheduler

internal class TimerForever(override val interval: Int) : Timer.Forever {

    override var nextRunTick: Int = 0
        private set

    override var completed: Boolean = false
        private set

    override fun update(currentTick: Int) {
        if (nextRunTick <= currentTick) {
            nextRunTick = currentTick + interval
        }
    }

    override fun start(tickCount: Int) {
        nextRunTick = tickCount
    }
}