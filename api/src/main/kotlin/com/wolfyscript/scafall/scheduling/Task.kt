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
import com.wolfyscript.scafall.function.ReceiverConsumer

interface Task {
    //    long delay();
    //    long interval();

    fun cancel()

    fun plugin(): PluginWrapper

    interface Builder {
        fun async(): Builder

        fun delay(ticks: Long): Builder

        fun interval(ticks: Long): Builder

        fun execute(runnable: Runnable): Builder

        fun execute(executor: ReceiverConsumer<Task>): Builder

        fun build(): Task
    }
}
