package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import com.fasterxml.jackson.databind.type.TypeFactory
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registry
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

    private lateinit var superType: JavaType

    override fun init(baseType: JavaType) {
        superType = baseType
    }

    override fun idFromValue(value: Any): String? {
        return getKey(value)
    }

    override fun idFromValueAndType(value: Any, aClass: Class<*>?): String? {
        return getKey(value)
    }

    private fun getKey(value: Any): String? {
        val baseType = getBaseClassType()
        val key = getTypedKey(baseType, value)
        if (key != null) {
            return key.toString()
        }
        ScafallProvider.get().logger.error("Failed to resolve type id for '$value' of base type '$baseType'! Is it registered?")
        return null
    }

    private fun <T> getTypedKey(baseType: Class<T>, value: Any): Key? {
        val registry = getAssociatedRegistry(baseType)
        if (registry == null) {
            ScafallProvider.get().logger.error("Failed to construct type id! ${getBaseClassType()} has no associated Registry!")
            return null
        }

        return registry.getKey(value::class.java as Class<out T>)
    }

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val baseType = getBaseClassType()
        val registry = getAssociatedRegistry(baseType)
        if (registry == null) {
            ScafallProvider.get().logger.error("Failed to get type for $id! $baseType has no associated Registry!")
            return TypeFactory.unknownType()
        }

        val namespace = superType.rawClass.getAnnotation(DefaultNamespace::class.java)?.namespace ?: registry.key.namespace
        val key = if (id.contains(':')) {
            Key.parse(id)
        } else {
            // Complete the key with the default namespace if it isn't yet.
            // It assumes that the default namespace is equal to the namespace of the registry (alternatively, it can be overwritten).
            Key.key(namespace, id)
        }

        val clazz = getTypeClass(key, registry)
        return if (clazz != null) {
            context.constructSpecializedType(superType, clazz)
        } else {
            if (key.namespace != namespace) {
                ScafallProvider.get().logger.error("Failed to get type of key $key for $baseType! May depend on a third-party. Please check your dependencies!")
            } else {
                // In case the namespace is the default, then it is most likely a mistake by the dev or configuration
                ScafallProvider.get().logger.error("Failed to get type of key $key for ${baseType}! Is it registered?")
            }
            TypeFactory.unknownType()
        }
    }

    /**
     * Gets the base type of this resolver, which the underlying lookup registry uses.
     */
    private fun getBaseClassType(): Class<*> {
        val rawClass = superType.rawClass
        //If it is specified, use the custom base type instead.
        val baseTypeAnnot = rawClass.getDeclaredAnnotation(KeyedBaseType::class.java)
        if (baseTypeAnnot != null) {
            return baseTypeAnnot.baseType.java
        }
        return rawClass
    }

    private fun <T> getAssociatedRegistry(type: Class<T>): Registry<Class<out T>>? {
        //Get the registry of the required base type
        return TYPE_REGISTRIES[type] as Registry<Class<out T>>?
    }

    private fun getTypeClass(key: Key?, registry: Registry<out Class<*>>): Class<*>? {
        if (key != null) {
            //Get the registry of the required base type
            return registry[key]
        }
        return null
    }

    override fun getMechanism(): JsonTypeInfo.Id {
        return JsonTypeInfo.Id.CUSTOM
    }

    companion object {
        private val TYPE_REGISTRIES: MutableMap<Class<*>, Registry<Class<out Any>>> = HashMap()

        /**
         * Registers a registry to be used for Json serialization and deserialization. <br></br>
         * To use that the class of the specified type must be annotated with [OptionalKeyReference].
         *
         * @param type The type to register.
         * @param registry The registry of the specified type.
         * @param <T> The type of the object.
        </T> */
        fun <T> registerTypeRegistry(type: Class<T>, registry: TypeRegistry<T>) {
            TYPE_REGISTRIES.putIfAbsent(type, registry as Registry<Class<out Any>>)
        }

        /**
         * Registers a registry to be used for Json serialization and deserialization. <br></br>
         * To use that the class of the specified type must be annotated with [OptionalKeyReference].
         *
         * @param type The type to register.
         * @param registry The registry of the specified type.
         * @param <T> The type of the object.
        </T> */
        fun <T> registerTypeRegistry(type: Class<T>, registry: Registry<Class<out T>>) {
            TYPE_REGISTRIES.putIfAbsent(type, registry as Registry<Class<out Any>>)
        }

    }
}
