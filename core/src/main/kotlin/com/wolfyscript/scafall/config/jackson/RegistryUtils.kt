package com.wolfyscript.scafall.config.jackson

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registry
import com.wolfyscript.scafall.registry.TypeRegistry

private val TYPE_REGISTRIES: MutableMap<Class<*>, Registry<Class<out Any>>> = HashMap()

/**
 * Registers a registry to be used for Json serialization and deserialization.
 *
 * @param type The type to register.
 * @param registry The registry of the specified type.
 * @param <T> The type of the object.
</T> */
fun <T> registerTypeRegistry(type: Class<T>, registry: TypeRegistry<T>) {
    TYPE_REGISTRIES.putIfAbsent(type, registry as Registry<Class<out Any>>)
}

/**
 * Registers a registry to be used for Json serialization and deserialization.
 *
 * @param type The type to register.
 * @param registry The registry of the specified type.
 * @param <T> The type of the object.
</T> */
fun <T> registerTypeRegistry(type: Class<T>, registry: Registry<Class<out T>>) {
    TYPE_REGISTRIES.putIfAbsent(type, registry as Registry<Class<out Any>>)
}

internal fun <T> getAssociatedTypeRegistry(type: Class<T>): Registry<Class<out T>>? {
    //Get the registry of the required base type
    return TYPE_REGISTRIES[type] as Registry<Class<out T>>?
}

internal fun getTypeClass(key: Key?, registry: Registry<out Class<*>>): Class<*>? {
    if (key != null) {
        //Get the registry of the required base type
        return registry[key]
    }
    return null
}

/**
 * Gets the base type of the raw type, which the underlying lookup registry uses.
 */
internal fun resolveBaseClassType(rawType: Class<*>): Class<*> {
    //If it is specified, use the custom base type instead.
    val baseTypeAnnot = rawType.getDeclaredAnnotation(KeyedBaseType::class.java)
    if (baseTypeAnnot != null) {
        return baseTypeAnnot.baseType.java
    }
    return rawType
}

internal fun getKeyForType(rawClass: Class<*>, value: Any): String? {
    val baseType = resolveBaseClassType(rawClass)
    val key = getTypesafeKeyForType(baseType, value)
    if (key != null) {
        return key.toString()
    }
    ScafallProvider.get().logger.error("Failed to resolve type id for '$value' of base type '$baseType'! Is it registered?")
    return null
}

internal fun <T> getTypesafeKeyForType(baseType: Class<T>, value: Any): Key? {
    val registry = getAssociatedTypeRegistry(baseType)
    if (registry == null) {
        ScafallProvider.get().logger.error("Failed to construct type id! $baseType has no associated Registry!")
        return null
    }
    return registry.getKey(value::class.java as Class<out T>)
}