package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException

/**
 * Abstract base class for custom serializers that handle specific value types during Jackson serialization.
 *
 * This abstract class provides a foundation for creating custom serializers that can handle
 * special serialization logic for specific types. Implementations of this class are used
 * by the [OptionalValueSerializer] annotation to provide custom serialization behaviour.
 *
 * The key feature of this serializer is its return value from the [serialize] method:
 * - Return `true` if the serialization was handled by this serializer
 * - Return `false` if the serialization should be delegated to the default serializer
 *
 * @param T The type of objects this serializer handles.
 */
abstract class ValueSerializer<T> protected constructor(var type: Class<T>) {
    /**
     * Serializes the given object using the provided JsonGenerator.
     *
     * This method is called during serialization to handle the actual serialization
     * of the object. Implementations should write the serialized representation
     * directly to the JsonGenerator.
     *
     * @param targetObject The object to serialize
     * @param generator The JsonGenerator to write to
     * @param provider The SerializerProvider providing additional context
     * @return true if this serializer handled the serialization, false if it should be delegated to the default serializer
     * @throws IOException if an I/O error occurs during serialization
     */
    @Throws(IOException::class)
    abstract fun serialize(targetObject: T, generator: JsonGenerator, provider: SerializerProvider): Boolean
}
