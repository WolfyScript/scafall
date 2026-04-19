package com.wolfyscript.scafall.scheduling

import java.util.UUID

internal interface ScafallTask : Task {

    val timer: Timer

    val delay: Delay

    val id: UUID

    val sync: Boolean

    var nextRunTicks: Long

    val isCompleted: Boolean

    fun run()

}