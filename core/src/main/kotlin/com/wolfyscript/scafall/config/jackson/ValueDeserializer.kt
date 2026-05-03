package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import java.io.IOException

/**
 * Abstract base class for custom deserializers that handle specific value types during Jackson deserialization.
 *
 * This abstract class provides a foundation for creating custom deserializers that can handle
 * special deserialization logic for specific types. Implementations of this class are used
 * by the [OptionalValueDeserializer] annotation to provide custom deserialization behaviour.
 *
 * This deserializer is particularly useful when dealing with configuration values that may have special formats
 * or require custom parsing logic.
 *
 * @param T The type of objects this deserializer handles.
 */
abstract class ValueDeserializer<T> protected constructor(var type: Class<T>) {
    /**
     * Deserializes the given JSON content into an object of type T.
     *
     * This method is called during deserialization to convert the JSON data into
     * the appropriate object. Implementations should parse the JsonParser
     * and return the deserialized object.
     *
     * @param p The JsonParser containing the JSON content to deserialize
     * @param ctxt The DeserializationContext providing additional deserialization information
     * @return The deserialized object, or null to fall back to default deserialization
     * @throws IOException if an I/O error occurs during deserialization
     */
    @Throws(IOException::class)
    abstract fun deserialize(p: JsonParser, ctxt: DeserializationContext): T?
}
