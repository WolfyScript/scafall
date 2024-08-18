package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.executableblocks

import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

interface ExecutableBlocksIntegration : PluginIntegration {
    /**
     * Verify if id is a valid ExecutableBlock ID
     *
     * @param id The ID to verify
     * @return true if it is a valid ID, false otherwise
     */
    fun isValidID(id: String): Boolean

    /**
     * Get all ExecutableBlock Ids
     *
     * @return All ExecutableBlock ids
     */
    val executableBlockIdsList: List<String>

    /**
     * Gets the ExecutableBlock id that belongs to the specified ItemStack.
     *
     * @param stack The ItemStack that belongs to an ExecutableBlock
     * @return The id of the ExecutableBlock or an empty Optional.
     */
    fun getExecutableBlock(stack: ItemStack?): String?

    companion object {
        const val PLUGIN_NAME: String = "ExecutableBlocks"
        val BLOCK_ID: NamespacedKey = NamespacedKey(PLUGIN_NAME.lowercase(), "eb-id")
    }
}
