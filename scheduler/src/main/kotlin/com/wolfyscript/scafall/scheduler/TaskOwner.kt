package com.wolfyscript.scafall.scheduler

import java.util.UUID

/**
 * Identifies the owner of a task.
 *
 * Used to determine who is responsible for a task, and allows to cancel tasks of a specific owner, for example if that owner is no longer available.
 * Like a mod or plugin that created a task gets disabled or unloaded, then all tasks created by that mod or plugin should be cancelled.
 */
interface TaskOwner {

    val id: UUID

}