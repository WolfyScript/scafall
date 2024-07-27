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
                if (genericType.isAssignableFrom(constructedDeserializer.getType())) {
                    this.deserializer = constructedDeserializer as ValueDeserializer<T>
                } else {
                    throw IllegalArgumentException("ValueDeserializer of type \"" + constructedDeserializer.getType().name + "\" cannot construct type \"" + genericType.name + "\"")
                }
            }

            @Throws(IOException::class)
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
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
            ): Any {
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
