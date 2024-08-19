package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.executableblocks

import com.ssomar.executableblocks.executableblocks.ExecutableBlocksManager
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationAbstract
import com.wolfyscript.scafall.spigot.platform.compatibility.WUPluginIntegration
import com.wolfyscript.scafall.spigot.platform.compatibility.plugins.executableblocks.ExecutableBlocksIntegration.Companion.BLOCK_ID
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

@WUPluginIntegration(pluginName = ExecutableBlocksIntegration.PLUGIN_NAME)
internal class ExecutableBlocksImpl(core: Scafall) : PluginIntegrationAbstract(core, ExecutableBlocksIntegration.PLUGIN_NAME),
    ExecutableBlocksIntegration {

    private lateinit var manager: ExecutableBlocksManager

    override fun init(plugin: Plugin?) {
        this.manager = ExecutableBlocksManager.getInstance()
        core.registries.stackIdentifierParsers.register(ExecutableBlocksStackIdentifier.Parser(this, manager))
    }

    override fun hasAsyncLoading(): Boolean {
        return false
    }

    override fun isValidID(id: String): Boolean {
        return manager.isValidID(id)
    }

    override val executableBlockIdsList: List<String>
        get() = manager.executableBlockIdsList

    override fun getExecutableBlock(stack: ItemStack?): String? {
        if (stack == null || stack.itemMeta == null || stack.itemMeta.persistentDataContainer.isEmpty) return null
        return stack.itemMeta.persistentDataContainer.get(
            BLOCK_ID,
            PersistentDataType.STRING
        )?.let {
            if (manager.isValidID(it)) it else null
        }
    }
}
