package com.wolfyscript.scafall.core

import com.wolfyscript.scafall.scheduler.TaskOwner

/**
 * Represents a mod wrapper that provides access to the mod's name and logger.
 */
interface ModIdentifier : TaskOwner {

    /**
     * The name of the mod.
     */
    val id: String

}