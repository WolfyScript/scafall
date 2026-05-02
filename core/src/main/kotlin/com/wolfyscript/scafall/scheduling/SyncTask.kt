package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper
import java.util.UUID

/**
 * A synchronous task that runs on the main thread and executes the provided function according to
 * the specified timer and delay settings.
 *
 * @property fn The function to execute when the task runs.
 * @property timer The timer configuration that determines how often and when the task executes.
 * @property delay The initial delay before the task begins execution.
 * @property id A unique identifier for this task instance.
 * @property mod The mod wrapper associated with this task, providing access to mod-specific resources.
 */
internal class SyncTask(
    val fn: () -> Unit,
    override val timer: Timer = Timer.Once,
    override val delay: Delay = Delay.Instant,
    override val id: UUID = UUID.randomUUID(),
    override val mod: ModWrapper,
) : ScafallTask {

    override var nextRunTicks: Long = 0
    override var isCompleted: Boolean = false
        private set
    override val sync: Boolean = false

    override fun run() {
        if (!isCompleted) {
            fn()
        }
    }

    override fun cancel() {
        isCompleted = true
    }

}