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

import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder
import com.wolfyscript.scaffolding.identifier.Keyed

class KeyedTypeResolver : StdTypeResolverBuilder() {
    override fun buildTypeSerializer(
        config: SerializationConfig,
        baseType: JavaType,
        subtypes: Collection<NamedType>
    ): TypeSerializer? {
        return if (useForType(baseType)) super.buildTypeSerializer(config, baseType, subtypes) else null
    }

    override fun buildTypeDeserializer(
        config: DeserializationConfig,
        baseType: JavaType,
        subtypes: Collection<NamedType>
    ): TypeDeserializer? {
        return if (useForType(baseType)) super.buildTypeDeserializer(config, baseType, subtypes) else null
    }

    fun useForType(t: JavaType): Boolean {
        return t.isTypeOrSubTypeOf(Keyed::class.java)
    }
}
