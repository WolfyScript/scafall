package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper
import java.util.UUID

internal class SyncTask(
    override val fn: Task.() -> Unit,
    override val repeat: Repeat = Repeat.Never,
    override val delay: Delay = Delay.Instant,
    override val id: UUID = UUID.randomUUID(),
    val mod: ModWrapper,
) : ScafallTask {

    override var nextRunTicks: Long = 0
    override var isCompleted: Boolean = false
        private set
    override val sync: Boolean = false

    override fun run() {
        if (!isCompleted) {
            this.fn()
        }
    }

    override fun complete() {
        isCompleted = true
    }

    override fun cancel() = complete()

    override fun plugin(): ModWrapper = mod
}