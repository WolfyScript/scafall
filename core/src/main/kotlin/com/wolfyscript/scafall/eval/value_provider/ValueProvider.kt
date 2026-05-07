package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.wolfyscript.scafall.config.jackson.OptionalValueDeserializer
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.RegistryKeyTypeIdResolver
import com.wolfyscript.scafall.eval.context.EvalContext
import java.io.IOException
import java.util.regex.Pattern

@JsonTypeIdResolver(RegistryKeyTypeIdResolver::class)
@OptionalValueDeserializer(deserializer = ValueProvider.ValueDeserializer::class)
@OptionalValueSerializer(serializer = ValueProvider.ValueSerializer::class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["key"])
interface ValueProvider<V> {

    @JsonIgnore
    fun getValue(context: EvalContext): V

    @get:JsonIgnore
    val value: V
        get() = getValue(EvalContext())

    class ValueDeserializer :
        com.wolfyscript.scafall.config.jackson.ValueDeserializer<ValueProvider<*>>(ValueProvider::class.java) {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ValueProvider<*>? {
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                val node = p.readValueAsTree<JsonNode>()
                val text = node.asText()
                if (text.isNotBlank()) {
                    val matcher = NUM_PATTERN.matcher(text)
                    if (matcher.matches()) {
                        val value: String
                        var id = matcher.group(2)
                        if (id != null) {
                            // integer value
                            value = matcher.group(1)
                        } else {
                            // float value
                            id = matcher.group(4)
                            value = matcher.group(3)
                        }
                        try {
                            return when (id!![0]) {
                                's', 'S' -> ValueProviderShortConst(value.toShort())
                                'i', 'I' -> ValueProviderIntegerConst(value.toInt())
                                'l', 'L' -> ValueProviderLongConst(value.toLong())
                                'f', 'F' -> ValueProviderFloatConst(value.toFloat())
                                'd', 'D' -> ValueProviderDoubleConst(value.toDouble())
                                else -> ValueProviderStringConst(text)
                            }
                        } catch (e: NumberFormatException) {
                            // Cannot parse the value. Might a String value!
                        }
                    }
                    return ValueProviderStringConst(text)
                }
            } else if (p.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                return ValueProviderIntegerConst(ctxt.readValue(p, Int::class.java))
            } else if (p.currentToken() == JsonToken.VALUE_NUMBER_FLOAT) {
                return ValueProviderDoubleConst(ctxt.readValue(p, Double::class.java))
            }
            return null
        }

        companion object {
            private val NUM_PATTERN: Pattern = Pattern.compile("([0-9]+)([bBsSiIlL])|([0-9]?\\.?[0-9])+([fFdD])")
        }
    }

    class ValueSerializer :
        com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProvider<*>>(ValueProvider::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: ValueProvider<*>,
            generator: JsonGenerator,
            provider: SerializerProvider,
        ): Boolean {
            return when (targetObject) {
                is ValueProviderStringConst -> {
                    generator.writeString(targetObject.value)
                    true
                }

                is ValueProviderByteConst -> {
                    generator.writeString("${targetObject.value}b")
                    true
                }

                is ValueProviderShortConst -> {
                    generator.writeString("${targetObject.value.toByte()}s")
                    true
                }

                is ValueProviderIntegerConst -> {
                    generator.writeNumber(targetObject.value)
                    true
                }

                is ValueProviderLongConst -> {
                    generator.writeString("${targetObject.value}L")
                    true
                }

                is ValueProviderFloatConst -> {
                    generator.writeString("${targetObject.value}f")
                    true
                }

                is ValueProviderDoubleConst -> {
                    generator.writeString("${targetObject.value}d")
                    true
                }

                else -> false
            }
        }
    }
}
