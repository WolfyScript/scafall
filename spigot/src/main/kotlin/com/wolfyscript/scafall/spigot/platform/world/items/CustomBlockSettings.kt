package com.wolfyscript.scafall.spigot.platform.world.items

import com.wolfyscript.scafall.Copyable
import java.util.*

class CustomBlockSettings : Copyable<CustomBlockSettings> {
    var isUseCustomDrops: Boolean

    constructor() {
        this.isUseCustomDrops = true
    }

    private constructor(settings: CustomBlockSettings) {
        this.isUseCustomDrops = settings.isUseCustomDrops
    }

    override fun copy(): CustomBlockSettings {
        return CustomBlockSettings(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CustomBlockSettings
        return isUseCustomDrops == that.isUseCustomDrops
    }

    override fun hashCode(): Int {
        return Objects.hash(isUseCustomDrops)
    }
}
