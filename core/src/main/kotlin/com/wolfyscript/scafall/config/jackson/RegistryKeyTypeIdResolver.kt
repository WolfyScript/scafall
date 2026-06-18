package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import com.fasterxml.jackson.databind.type.TypeFactory
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.TypeRegistry

/**
 * Used as a [TypeIdResolver][com.fasterxml.jackson.databind.jsontype.TypeIdResolver] for [JsonTypeIdResolver][com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver] to
 * serialize and deserialize custom types that are available in a [TypeRegistry].
 *
 * To determine the [Key] and Class of the object it needs to associate it with a registry.
 * A registry for a type must be registered, see [registerTypeRegistry].
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
        return getKeyForType(superType.rawClass, value)
    }

    override fun idFromValueAndType(value: Any, aClass: Class<*>?): String? {
        return getKeyForType(superType.rawClass, value)
    }

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val baseType = resolveBaseClassType(superType.rawClass)
        val registry = getAssociatedTypeRegistry(baseType)
        if (registry == null) {
            ScafallProvider.get().logger.error("Failed to get type for $id! $baseType has no associated Registry!")
            return TypeFactory.unknownType()
        }

        val namespace = superType.rawClass.getAnnotation(DefaultNamespace::class.java)?.namespace ?: registry.key.namespace
        val key = Key.parse(namespace, id)

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

    override fun getMechanism(): JsonTypeInfo.Id {
        return JsonTypeInfo.Id.CUSTOM
    }

}
