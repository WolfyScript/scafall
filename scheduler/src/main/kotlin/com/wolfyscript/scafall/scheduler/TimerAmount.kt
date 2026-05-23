package com.wolfyscript.scafall.scheduler

internal class TimerAmount(
    override var amount: Int,
    override val interval: Int,
) : Timer.Amount {

    override var nextRunTick: Int = 0
        private set

    override var completed: Boolean = false
        private set

    override fun update(currentTick: Int) {
        if (nextRunTick <= currentTick) {
            nextRunTick = currentTick + interval
        }
        if (--amount <= 0) {
            completed = true
        }
    }

    override fun start(tickCount: Int) {
        nextRunTick = tickCount
    }

}