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

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.key
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.registry.Registry
import java.io.IOException
import java.lang.reflect.Field

/**
 * This annotation allows serializing/deserializing an instance of [Keyed] by its key or value.
 * The annotated type **must** have a [Registry], that contains the class of the type.<br></br>
 * The specified key must exist and be type of [Key].<br></br>
 *
 * <br></br>Serialization:<br></br>
 * Serializes the key if available, else if key is null uses default serializer.<br></br>
 *
 * <br></br>Deserialization:<br></br>
 * If the [JsonNode] is text it will convert it to [Key] and look for the value in the [Registry] of the objects type.
 * <br></br>
 * Looks for the value in the registry:
 * <pre>`
 * {
 * "value": "wolfyutils:registered_value"
 * }
`</pre> *
 * Uses the custom value specified:
 * <pre>`
 * {
 * "value": {
 * "custom_object_val": 7,
 * ...
 * }
 * }
`</pre> *
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class OptionalKeyReference(
    /**
     * The field name of the [Key] that identifies the object.
     *
     * @return the field name of the objects' key
     */
    val field: String = "key",
    /**
     * Tells the serializer if it should serialize any object of this type as a key reference.<br></br>
     * On by default, will always serialize as a key reference. This uses the field specified in [.field]!<br></br>
     * If disabled the value of [.field] is ignored and the object is always serialized using the default serializer.
     *
     * @return If it should be serialized as a key reference.
     */
    val serializeAsKey: Boolean = true, val registryKey: String = ""
) {
    class SerializerModifier : BeanSerializerModifier() {
        override fun modifySerializer(
            config: SerializationConfig,
            beanDesc: BeanDescription,
            serializer: JsonSerializer<*>
        ): JsonSerializer<*> {
            val handledType = serializer.handledType()
            val annotation = handledType.getAnnotation(OptionalKeyReference::class.java)
            if (annotation != null) {
                if (Keyed::class.java.isAssignableFrom(handledType)) {
                    return Serializer(annotation, serializer as JsonSerializer<out Keyed>)
                }
            }
            return serializer
        }

        private class Serializer<T : Keyed>(
            private val reference: OptionalKeyReference,
            private val defaultSerializer: JsonSerializer<T>
        ) :
            StdSerializer<T>(defaultSerializer.handledType()) {
            private val property = reference.field

            @Throws(IOException::class)
            override fun serialize(targetObject: T, generator: JsonGenerator, provider: SerializerProvider) {
                if (reference.serializeAsKey) {
                    try {
                        val propertyField: Field = targetObject.javaClass.getDeclaredField(property)
                        propertyField.isAccessible = true
                        val propertyObject = propertyField[targetObject]
                        if (propertyObject is Key) {
                            generator.writeObject(propertyObject)
                            return
                        }
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }
                defaultSerializer.serialize(targetObject, generator, provider)
            }
        }
    }

    class DeserializerModifier(private val core: Scafall) : BeanDeserializerModifier() {
        override fun modifyDeserializer(
            config: DeserializationConfig,
            beanDesc: BeanDescription,
            deserializer: JsonDeserializer<*>
        ): JsonDeserializer<*> {
            val handledType = deserializer.handledType()
            val annotation = handledType.getAnnotation(OptionalKeyReference::class.java)
            if (annotation != null) {
                if (Keyed::class.java.isAssignableFrom(handledType)) {
                    return Deserializer(
                        core, annotation, deserializer as JsonDeserializer<out Keyed>
                    )
                }
            }
            return deserializer
        }

        private class Deserializer<T : Keyed>(
            private val core: Scafall,
            reference: OptionalKeyReference,
            private val defaultDeserializer: JsonDeserializer<T>
        ) :
            StdDeserializer<T>(defaultDeserializer.handledType()), ResolvableDeserializer {
            private val genericType: Class<T> = defaultDeserializer.handledType() as Class<T>
            private val registryKey = key(Key.SCAFFOLDING_NAMESPACE, reference.registryKey)

            @Throws(IOException::class)
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T? {
                if (p.isExpectedStartObjectToken) {
                    return defaultDeserializer.deserialize(p, ctxt)
                }
                return getKeyedObject(p)
            }

            @Throws(JsonMappingException::class)
            override fun resolve(ctxt: DeserializationContext) {
                if (defaultDeserializer is ResolvableDeserializer) {
                    defaultDeserializer.resolve(ctxt)
                }
            }

            @Throws(IOException::class)
            override fun deserializeWithType(
                p: JsonParser,
                ctxt: DeserializationContext,
                typeDeserializer: TypeDeserializer
            ): Any? {
                if (p.isExpectedStartObjectToken) {
                    return defaultDeserializer.deserializeWithType(p, ctxt, typeDeserializer)
                }
                return getKeyedObject(p)
            }

            @Throws(IOException::class)
            fun getKeyedObject(p: JsonParser): T? {
                val registry = core.registries.getByKey(registryKey) as Registry<T>?
                if (registry != null) {
                    val value = p.readValueAs(String::class.java)
                    return registry.get(key(Key.SCAFFOLDING_NAMESPACE, value))
                }
                return null
            }
        }
    }
}
