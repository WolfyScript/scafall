package com.wolfyscript.scaffolding

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import java.util.*

/**
 * A wrapper for the Adventure API Audiences.
 *
 * This wrapper tries to provide cross-platform audiences as well as possible.
 * The platform specific implementation must be used in cases where this wrapper may be lacking features or audiences!
 */
interface AdventureUtil {

    fun player(uuid: UUID) : Audience

    fun all() : Audience

    fun system() : Audience

    val miniMsg: MiniMessage

}