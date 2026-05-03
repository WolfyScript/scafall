package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.wolfyscript.scafall.identifier.Namespaced
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Annotation used to define a custom serializer for optional values in Jackson serialization.
 *
 * This annotation allows specifying a [ValueSerializer] to handle serialization of optional
 * values, with the option to delegate to the default serializer when the custom serializer
 * indicates it doesn't handle the value.
 *
 * The primary use case for this annotation is to handle configuration values that may be
 * represented differently during serialization. It allows for flexible serialization where
 * a custom serializer can handle special cases, while falling back to the default serializer
 * for standard cases.
 *
 * For example, configuration values might be represented as:
 * - Simple values (e.g., "value")
 * - Objects with specific structure (e.g., {"value": "something"})
 * - Null values
 *
 * The annotation can be used on classes that represent configuration models, where some fields
 * might require special handling or formatting during serialization.
 *
 * @property serializer The [KClass] of the [ValueSerializer] to use for serializing optional values.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class OptionalValueSerializer(val serializer: KClass<out ValueSerializer<*>>) {

    class SerializerModifier : BeanSerializerModifier() {
        override fun modifySerializer(
            config: SerializationConfig,
            beanDesc: BeanDescription,
            serializer: JsonSerializer<*>
        ): JsonSerializer<*> {
            val handledType = serializer.handledType()
            val annotation = handledType.getAnnotation(
                OptionalValueSerializer::class.java
            )
            if (annotation != null) {
                try {
                    return Serializer(annotation, serializer as JsonSerializer<Namespaced>)
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            return serializer
        }

        /**
         * Custom serializer that handles optional values with a specified [ValueSerializer].
         *
         * This inner class wraps the default serializer and delegates to a custom [ValueSerializer]
         * when needed. It only delegates to the default serializer when the custom serializer
         * indicates it doesn't handle the value.
         *
         * This serializer is particularly useful for handling configuration values that may be
         * represented in multiple formats or require special formatting during serialization.
         *
         * @param <T> The type of the serialized object.
         */
        private class Serializer<T : Namespaced?>(
            reference: OptionalValueSerializer,
            private val defaultSerializer: JsonSerializer<T>
        ) : StdSerializer<T>(defaultSerializer.handledType()) {
            private val genericType: Class<T> = defaultSerializer.handledType()
            private var serializer: ValueSerializer<T>? = null

            init {
                val constructedDeserializer: ValueSerializer<*> =
                    reference.serializer.primaryConstructor?.call() ?: throw IllegalArgumentException("No primary constructor found")
                if (genericType.isAssignableFrom(constructedDeserializer.type)) {
                    this.serializer = constructedDeserializer as ValueSerializer<T>
                } else {
                    throw IllegalArgumentException("ValueSerializer of type \"" + constructedDeserializer.type.name + "\" cannot handle type \"" + genericType.name + "\"")
                }
            }

            @Throws(IOException::class)
            override fun serializeWithType(
                value: T,
                gen: JsonGenerator,
                serializers: SerializerProvider,
                typeSer: TypeSerializer
            ) {
                if (!serializer!!.serialize(value, gen, serializers)) {
                    defaultSerializer.serializeWithType(value, gen, serializers, typeSer)
                }
            }

            @Throws(IOException::class)
            override fun serialize(targetObject: T, generator: JsonGenerator, provider: SerializerProvider) {
                if (!serializer!!.serialize(targetObject, generator, provider)) {
                    defaultSerializer.serialize(targetObject, generator, provider)
                }
            }
        }
    }
}
