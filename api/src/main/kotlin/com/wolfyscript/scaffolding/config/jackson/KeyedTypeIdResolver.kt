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

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import com.fasterxml.jackson.databind.type.TypeFactory
import com.google.inject.Inject
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.key
import com.wolfyscript.scaffolding.identifier.Keyed
import com.wolfyscript.scaffolding.registry.Registry
import com.wolfyscript.scaffolding.registry.TypeRegistry

class KeyedTypeIdResolver : TypeIdResolverBase() {
    private var superType: JavaType? = null

    override fun init(baseType: JavaType) {
        superType = baseType
    }

    override fun idFromValue(value: Any): String {
        return getKey(value)
    }

    override fun idFromValueAndType(value: Any, aClass: Class<*>?): String {
        return getKey(value)
    }

    private fun getKey(value: Any): String {
        if (value is Keyed) {
            return value.key().toString()
        }
        throw IllegalArgumentException(String.format("Object %s is not of type Keyed!", value.javaClass.name))
    }

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val clazz = getTypeClass(key(Key.SCAFFOLDING_NAMESPACE, id))
        return if (clazz != null) context.constructSpecializedType(superType, clazz) else TypeFactory.unknownType()
    }

    protected fun getTypeClass(key: Key?): Class<*>? {
        if (key != null) {
            var rawClass = superType!!.rawClass
            //If it is specified, use the custom base type instead.
            val baseTypeAnnot = rawClass.getDeclaredAnnotation(KeyedBaseType::class.java)
            if (baseTypeAnnot != null) {
                rawClass = baseTypeAnnot.baseType.java
            }
            //Get the registry of the required base type
            val registry = TYPE_REGISTRIES[rawClass]
            if (registry != null) {
                val obj = registry.get(key)
                if (obj is Class<*>) {
                    return obj
                } else if (obj is Keyed) {
                    return obj.javaClass
                }
            }
        }
        return null
    }

    override fun getMechanism(): JsonTypeInfo.Id {
        return JsonTypeInfo.Id.CUSTOM
    }

    companion object {
        private val TYPE_REGISTRIES: MutableMap<Class<*>, Registry<*>> = HashMap()

        /**
         * Registers a registry to be used for Json serialization and deserialization. <br></br>
         * To use that the class of the specified type must be annotated with [OptionalKeyReference].
         *
         * @param type The type to register.
         * @param registry The registry of the specified type.
         * @param <T> The type of the object.
        </T> */
        fun <T : Keyed> registerTypeRegistry(type: Class<T>, registry: Registry<T>) {
            TYPE_REGISTRIES.putIfAbsent(type, registry)
        }

        /**
         * Registers a registry to be used for Json serialization and deserialization. <br></br>
         * To use that the class of the specified type must be annotated with [OptionalKeyReference].
         *
         * @param type The type to register.
         * @param registry The registry of the specified type.
         * @param <T> The type of the object.
        </T> */
        fun <T : Keyed> registerTypeRegistry(type: Class<T>, registry: TypeRegistry<T>) {
            TYPE_REGISTRIES.putIfAbsent(type, registry)
        }
    }
}
