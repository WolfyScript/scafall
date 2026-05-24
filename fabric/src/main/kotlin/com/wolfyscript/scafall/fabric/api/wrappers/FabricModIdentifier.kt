package com.wolfyscript.scafall.fabric.api.wrappers

import com.wolfyscript.scafall.core.ModIdentifier
import net.fabricmc.loader.api.ModContainer

class FabricModIdentifier(mod: ModContainer) : ModIdentifier {

    override val id: String = mod.metadata.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FabricModIdentifier) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        val result = id.hashCode()
        return result
    }

}

fun ModContainer.identifier() : FabricModIdentifier {
    return FabricModIdentifier(this)
}