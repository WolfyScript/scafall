/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scafall.spigot.platform.compatibility

import com.wolfyscript.scafall.Scafall

class CompatibilityManagerBukkit(core: Scafall) : CompatibilityManager {
    private val pluginsBukkit = PluginsBukkit(core)
    override val plugins: Plugins = pluginsBukkit

    override fun init() {
        pluginsBukkit.init()
    }

    companion object {
        private val classes: MutableMap<String, Boolean> = HashMap()

        /**
         * Check if the specific class exists.
         *
         * @param path The path to the class to check for.
         * @return If the class exists.
         */
        fun hasClass(path: String): Boolean {
            if (classes.containsKey(path)) {
                return classes[path]!!
            }
            try {
                Class.forName(path)
                classes[path] = true
                return true
            } catch (e: Exception) {
                classes[path] = false
                return false
            }
        }
    }
}
