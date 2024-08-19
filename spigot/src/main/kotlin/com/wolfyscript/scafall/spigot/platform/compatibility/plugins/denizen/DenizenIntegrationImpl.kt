package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.denizen

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scafall.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scafall.spigot.platform.stackIdentifiers
import org.bukkit.plugin.Plugin

@WUPluginIntegration(pluginName = DenizenIntegrationImpl.PLUGIN_NAME)
internal class DenizenIntegrationImpl(core: Scafall) :
    PluginIntegrationAbstract(core, PLUGIN_NAME) {

    override fun init(plugin: Plugin?) {
        core.registries.stackIdentifierParsers.register(DenizenStackIdentifier.Parser())
        core.registries.stackIdentifiers.register(DenizenStackIdentifier::class.java)
    }

    override fun hasAsyncLoading(): Boolean {
        return false
    }

    companion object {
        const val PLUGIN_NAME: String = "Denizen"
    }
}
