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
package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.PluginWrapper

interface Scheduler {

    /**
     * Creates a new [Task Builder][Task.Builder] bound to the specified [PluginWrapper]
     *
     * @param plugin The plugin that owns this task
     * @return The new Task Builder
     */
    fun task(plugin: PluginWrapper): Task.Builder

    fun syncTask(plugin: PluginWrapper, task: Runnable): Task {
        return syncTask(plugin, task, 0)
    }

    fun syncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task

    fun asyncTask(plugin: PluginWrapper, task: Runnable): Task {
        return asyncTask(plugin, task, 0)
    }

    fun asyncTask(plugin: PluginWrapper, task: Runnable, delay: Long): Task

    fun syncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task

    fun asyncTimerTask(plugin: PluginWrapper, task: Runnable, delay: Long, interval: Long): Task
}
