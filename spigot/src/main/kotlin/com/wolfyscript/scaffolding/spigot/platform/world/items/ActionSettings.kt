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
package com.wolfyscript.scaffolding.spigot.platform.world.items

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.platform.world.items.actions.Data
import com.wolfyscript.scaffolding.spigot.platform.world.items.actions.Event
import java.util.*
import java.util.stream.Collectors

class ActionSettings {
    @JsonIgnore
    private var indexedEvents: Multimap<Key, Event<*>>

    init {
        this.indexedEvents = HashMultimap.create()
    }

    @JsonSetter("events")
    fun setEvents(events: List<Event<*>>) {
        indexedEvents = HashMultimap.create()
        for (event in events) {
            indexedEvents.put(event.key(), event)
        }
    }

    @get:JsonGetter("events")
    val events: Collection<Event<*>>
        get() = Collections.unmodifiableCollection(
            indexedEvents.values()
        )

    fun <T : Data> getEvents(key: Key, dataType: Class<T>): List<Event<T>> {
        return indexedEvents[key].stream()
            .filter { actionEvent -> dataType == actionEvent.dataType }
            .map { actionEvent -> actionEvent as Event<T> }
            .collect(Collectors.toList())
    }

    fun <T : Data> callEvent(key: Key, data: T) {
        getEvents(key, data::class.java as Class<T>).forEach { event ->
            event.call(
                get(), data
            )
        }
    }
}
