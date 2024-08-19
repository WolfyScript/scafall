package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.fancybags

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scafall.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scafall.spigot.platform.stackIdentifiers
import org.bukkit.plugin.Plugin

@WUPluginIntegration(pluginName = FancyBagsImpl.KEY)
class FancyBagsImpl
/**
 * The main constructor that is called whenever the integration is created.<br></br>
 *
 * @param core       The WolfyUtilCore.
 */
protected constructor(core: Scafall) :
    PluginIntegrationAbstract(core, KEY) {
    override fun init(plugin: Plugin?) {
        core.registries.stackIdentifierParsers.register(FancyBagsStackIdentifier.Parser())
        core.registries.stackIdentifiers.register(FancyBagsStackIdentifier::class.java)
    }

    override fun hasAsyncLoading(): Boolean {
        return false
    }

    companion object {
        const val KEY: String = "FancyBags"
    }
}
