package com.wolfyscript.scafall.adventure

import net.kyori.adventure.audience.Audience
import java.util.*

/**
 * A wrapper for the Adventure API Audiences.
 *
 * This wrapper tries to provide cross-platform audiences as well as possible.
 * The platform specific implementation must be used in cases where this wrapper may be lacking features or audiences!
 */
interface AdventureUtil {

    /**
     * Get an audience for a specific player by UUID.
     *
     * @param uuid The unique identifier of the player
     * @return An Audience representing the player
     */
    fun player(uuid: UUID): Audience

    /**
     * Get an audience for all players on the server.
     *
     * @return An Audience representing all players
     */
    fun all(): Audience

    /**
     * Get an audience for system messages.
     *
     * @return An Audience representing system messages
     */
    fun system(): Audience

}
