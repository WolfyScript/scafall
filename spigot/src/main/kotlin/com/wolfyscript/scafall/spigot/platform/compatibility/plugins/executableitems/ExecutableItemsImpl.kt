package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.executableitems

import com.ssomar.score.api.executableitems.ExecutableItemsAPI
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scafall.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scafall.spigot.platform.stackIdentifiers
import org.bukkit.plugin.Plugin

@WUPluginIntegration(pluginName = ExecutableItemsIntegration.PLUGIN_NAME)
internal class ExecutableItemsImpl(core: Scafall) :
    PluginIntegrationAbstract(core, ExecutableItemsIntegration.PLUGIN_NAME),
    ExecutableItemsIntegration {

    override fun init(plugin: Plugin?) {
        core.registries.stackIdentifierParsers.register(ExecutableItemsStackIdentifier.Parser(ExecutableItemsAPI.getExecutableItemsManager()))
        core.registries.stackIdentifiers.register(ExecutableItemsStackIdentifier::class.java)
    }

    override fun hasAsyncLoading(): Boolean {
        return false
    }

    override fun isValidID(id: String?): Boolean {
        return ExecutableItemsAPI.getExecutableItemsManager().isValidID(id)
    }

    override val executableItemIdsList: List<String>
        get() = ExecutableItemsAPI.getExecutableItemsManager()
            .executableItemIdsList
}
