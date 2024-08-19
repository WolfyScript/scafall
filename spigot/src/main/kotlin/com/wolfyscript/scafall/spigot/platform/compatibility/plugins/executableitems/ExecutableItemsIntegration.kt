package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.executableitems

import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegration

interface ExecutableItemsIntegration : PluginIntegration {
    /**
     * Verify if id is a valid ExecutableItem ID
     *
     * @param id The ID to verify
     * @return true if it is a valid ID, false otherwise
     */
    fun isValidID(id: String?): Boolean

    /**
     * Get all ExecutableItems Ids
     *
     * @return All ExecutableItems ids
     */
    val executableItemIdsList: List<String>

    companion object {
        const val PLUGIN_NAME: String = "ExecutableItems"
    }
}
