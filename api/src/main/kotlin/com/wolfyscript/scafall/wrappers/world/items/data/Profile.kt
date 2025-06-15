package com.wolfyscript.scafall.wrappers.world.items.data

import java.net.URL
import java.util.*

open class Profile(
    var id: UUID?,
    var name: String?,
    var textures: Textures
) {

    fun isComplete(): Boolean {
        return id != null && name != null && !textures.isEmpty()
    }

    open class Textures(
        var skin: URL?,
        var cape: URL?
    ) {

        fun isEmpty(): Boolean {
            return cape == null && skin == null
        }
    }

}
