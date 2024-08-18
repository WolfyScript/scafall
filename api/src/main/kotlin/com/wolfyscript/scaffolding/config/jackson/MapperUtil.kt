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
package com.wolfyscript.scaffolding.config.jackson

import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.wolfyscript.scaffolding.PluginWrapper
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get

/**
 * Used to manage and cache Jackson mappers.
 */
class MapperUtil(private val pluginWrapper: PluginWrapper) {
    /**
     * Gets the currently cached ObjectMapper.
     *
     * @return The cached ObjectMapper or null
     */
    /**
     * Sets the global ObjectMapper to the one specified.
     *
     * @param globalMapper The ObjectMapper to cache.
     */
    var globalMapper: ObjectMapper = ObjectMapper()

    /**
     * Gets the currently cached ObjectMapper and casts it to the specified type.<br></br>
     * May cause ClassCastException, if it cannot be cast to the type.
     *
     * @return The cached ObjectMapper or null
     */
    fun <M : ObjectMapper?> getGlobalMapper(mapperType: Class<M>): M {
        return mapperType.cast(globalMapper)
    }

    fun <M : ObjectMapper?> applyWolfyUtilsInjectableValues(mapper: M, injectableValues: InjectableValues.Std): M {
        mapper!!.setInjectableValues(
            injectableValues
                .addValue(Scaffolding::class.java, get())
                .addValue(PluginWrapper::class.java, pluginWrapper)
        )
        return mapper
    }

    companion object {
        private fun <T> addSerializer(module: SimpleModule, serializer: JsonSerializer<T>) {
            module.addSerializer(serializer.handledType(), serializer)
        }

        private fun <T> addDeserializer(module: SimpleModule, deserializer: JsonDeserializer<T>) {
            module.addDeserializer(deserializer.handledType() as Class<in T>, deserializer)
        }
    }
}
