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

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.wolfyscript.scaffolding.identifier.Namespaced
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

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

        private class Serializer<T : Namespaced?>(
            reference: OptionalValueSerializer,
            private val defaultSerializer: JsonSerializer<T>
        ) : StdSerializer<T>(defaultSerializer.handledType()) {
            private val genericType: Class<T> = defaultSerializer.handledType()
            private var serializer: ValueSerializer<T>? = null

            init {
                val constructedDeserializer: ValueSerializer<*> =
                    reference.serializer.primaryConstructor?.call() ?: throw IllegalArgumentException("No primary constructor found")
                if (genericType.isAssignableFrom(constructedDeserializer.getType())) {
                    this.serializer = constructedDeserializer as ValueSerializer<T>
                } else {
                    throw IllegalArgumentException("ValueSerializer of type \"" + constructedDeserializer.getType().name + "\" cannot handle type \"" + genericType.name + "\"")
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
