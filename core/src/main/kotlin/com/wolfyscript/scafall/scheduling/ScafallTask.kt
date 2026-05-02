package com.wolfyscript.scafall.scheduling

/**
 * An internal extension of the [Task] interface, specifically designed for tasks managed by Scafall's scheduling system.
 *
 * This interface introduces [nextRunTicks], a property that tracks when the task should execute next based on tick counts,
 * and defines a contract for executing the task via the [run] function.
 */
internal interface ScafallTask : Task {

    var nextRunTicks: Long

    fun run()

}