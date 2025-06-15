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
package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import com.fasterxml.jackson.databind.type.TypeFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.TypeRegistry

/**
 * Used as a [TypeIdResolver][com.fasterxml.jackson.databind.jsontype.TypeIdResolver] for [JsonTypeIdResolver][com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver] to
 * serialize and deserialize custom types that are available in a [TypeRegistry].
 *
 * To determine the [Key] and Class of the object it needs to associate it with a registry.
 * A registry for a type must be registered, see [RegistryKeyTypeIdResolver.registerTypeRegistry].
 *
 * Upon de-/serialization it uses the type of the annotated class, or if available the type specified by [KeyedBaseType], to get the associated registry:
 *
 * - On serialization, the [Key] of the object is determined from the registry and stored as the type id.
 *
 * - On deserialization, the type id is parsed into [Key] and looked up in the registry.
 *
 */
class RegistryKeyTypeIdResolver : TypeIdResolverBase() {
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
        val baseType = getBaseClassType()
        val key = getTypedKey(baseType, value)
        if (key != null) {
            return key.toString()
        }
        throw IllegalStateException("Failed to resolve type id for object '$value' of base type '$baseType'! Is it registered?")
    }

    private fun <T> getTypedKey(baseType: Class<T>, value: Any): Key? {
        val registry = getAssociatedRegistry(baseType)
        if (registry == null) {
            throw IllegalArgumentException("Failed to construct type id: ${getBaseClassType()} has no associated Registry!")
        }

        return registry.getKey(value::class.java as Class<out T>)
    }

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val namespacedKey = if (id.contains(':')) {
            Key.parse(id)
        } else {
            Key.key(Key.SCAFFOLDING_NAMESPACE, id)
        }
        val clazz = getTypeClass(namespacedKey)
        return if (clazz != null) context.constructSpecializedType(superType, clazz) else TypeFactory.unknownType()
    }

    private fun getBaseClassType(): Class<*> {
        val rawClass = superType!!.rawClass
        //If it is specified, use the custom base type instead.
        val baseTypeAnnot = rawClass.getDeclaredAnnotation(KeyedBaseType::class.java)
        if (baseTypeAnnot != null) {
            return baseTypeAnnot.baseType.java
        }
        return rawClass
    }

    private fun <T> getAssociatedRegistry(type: Class<T>): TypeRegistry<T>? {
        //Get the registry of the required base type
        return TYPE_REGISTRIES[getBaseClassType()] as TypeRegistry<T>?
    }

    private fun getTypeClass(key: Key?): Class<*>? {
        if (key != null) {
            //Get the registry of the required base type
            val registry = getAssociatedRegistry(getBaseClassType())
            if (registry != null) {
                return registry[key]
            }
        }
        return null
    }

    override fun getMechanism(): JsonTypeInfo.Id {
        return JsonTypeInfo.Id.CUSTOM
    }

    companion object {
        private val TYPE_REGISTRIES: MutableMap<Class<*>, TypeRegistry<*>> = HashMap()

        /**
         * Registers a registry to be used for Json serialization and deserialization. <br></br>
         * To use that the class of the specified type must be annotated with [OptionalKeyReference].
         *
         * @param type The type to register.
         * @param registry The registry of the specified type.
         * @param <T> The type of the object.
        </T> */
        fun <T> registerTypeRegistry(type: Class<T>, registry: TypeRegistry<T>) {
            TYPE_REGISTRIES.putIfAbsent(type, registry)
        }
    }
}
