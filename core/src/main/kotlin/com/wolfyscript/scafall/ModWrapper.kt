package com.wolfyscript.scafall

import org.slf4j.Logger

/**
 * Represents a mod wrapper that provides access to the mod's name and logger.
 */
interface ModWrapper {

    /**
     * The name of the mod.
     */
    val name: String

    /**
     * The logger for the mod.
     */
    val logger: Logger

}
