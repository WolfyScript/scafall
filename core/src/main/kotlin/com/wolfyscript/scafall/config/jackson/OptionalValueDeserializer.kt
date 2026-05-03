package com.wolfyscript.scafall.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Annotation used to define a custom deserializer for optional values in Jackson deserialization.
 *
 * This annotation allows specifying a [ValueDeserializer] to handle deserialization of optional
 * values, with an option to always delegate to the [deserializer].
 *
 * The primary use case for this annotation is to handle configuration values that may be present
 * in different formats or may not be present at all. It allows for flexible deserialization
 * where a custom deserializer can handle special cases while falling back to the default
 * deserializer for standard cases.
 *
 * For example, configuration values might be represented as:
 * - Simple values (e.g., "value")
 * - Objects with specific structure (e.g., {"value": "something"})
 * - Null values
 *
 * The annotation can be used on classes that represent configuration models, where some fields
 * might require special handling or validation during deserialization.
 *
 * @property deserializer The [KClass] of the [ValueDeserializer] to use for deserializing optional values.
 * @property delegateObjectDeserializer Whether to also delegate to the [deserializer] when the deserialized value is an object.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class OptionalValueDeserializer(
    val deserializer: KClass<out ValueDeserializer<*>>,
    val delegateObjectDeserializer: Boolean = false
) {

    class DeserializerModifier : BeanDeserializerModifier() {
        override fun modifyDeserializer(
            config: DeserializationConfig,
            beanDesc: BeanDescription,
            deserializer: JsonDeserializer<*>
        ): JsonDeserializer<*> {
            val handledType = deserializer.handledType()
            val annotation = handledType.getAnnotation(
                OptionalValueDeserializer::class.java
            )
            if (annotation != null) {
                try {
                    return Deserializer(annotation, deserializer)
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                }
            }
            return deserializer
        }

        /**
         * Custom deserializer that handles optional values with a specified [ValueDeserializer].
         *
         * This inner class wraps the default deserializer and delegates to a custom [ValueDeserializer]
         * when needed. It can also delegate when the default object deserializer would have been used.
         *
         * This deserializer is particularly useful for handling configuration values that may be
         * represented in multiple formats, such as simple values or complex object structures.
         *
         * @param <T> The type of the deserialized object.
         */
        private class Deserializer<T : Any>(reference: OptionalValueDeserializer, defaultSerializer: JsonDeserializer<T>) :
            StdDeserializer<T>(defaultSerializer.handledType()), ResolvableDeserializer {
            private var deserializer: ValueDeserializer<T>? = null
            private val defaultDeserializer: JsonDeserializer<T>
            private val alwaysDelegate: Boolean

            init {
                val genericType = defaultSerializer.handledType() as Class<T>
                this.defaultDeserializer = defaultSerializer
                this.alwaysDelegate = reference.delegateObjectDeserializer
                val constructedDeserializer: ValueDeserializer<*> =
                    reference.deserializer.primaryConstructor?.call() ?: throw IllegalArgumentException("")
                if (genericType.isAssignableFrom(constructedDeserializer.type)) {
                    this.deserializer = constructedDeserializer as ValueDeserializer<T>
                } else {
                    throw IllegalArgumentException("ValueDeserializer of type \"" + constructedDeserializer.type.name + "\" cannot construct type \"" + genericType.name + "\"")
                }
            }

            @Throws(IOException::class)
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T? {
                if (p.isExpectedStartObjectToken) {
                    if (alwaysDelegate) {
                        val value = deserializer!!.deserialize(p, ctxt)
                        if (value != null) return value
                    }
                    return defaultDeserializer.deserialize(p, ctxt)
                }
                return deserializer!!.deserialize(p, ctxt)
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
                    if (alwaysDelegate) {
                        val value = deserializer!!.deserialize(p, ctxt)
                        if (value != null) return value
                    }
                    return defaultDeserializer.deserializeWithType(p, ctxt, typeDeserializer)
                }
                return deserializer!!.deserialize(p, ctxt)
            }
        }
    }
}
