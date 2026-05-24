package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.core.ModIdentifier
import com.wolfyscript.scafall.platform.into
import org.spongepowered.plugin.PluginContainer

class SpongePluginIdentifier(plugin: PluginContainer) : ModIdentifier {

    override val id: String = plugin.metadata().id()

}

fun PluginContainer.identifier() : SpongePluginIdentifier {
    return SpongePluginIdentifier(this)
}

internal fun ModIdentifier.into() : SpongePluginIdentifier {
    return into<SpongePluginIdentifier>()
}
