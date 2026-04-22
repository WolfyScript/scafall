package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.node.ObjectNode
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider.Companion.get
import com.wolfyscript.scafall.config.jackson.RegistryKeyTypeIdResolver
import com.wolfyscript.scafall.config.jackson.OptionalValueDeserializer
import com.wolfyscript.scafall.config.jackson.ValueDeserializer
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import java.io.IOException
import java.util.regex.Pattern

@JsonTypeIdResolver(
    RegistryKeyTypeIdResolver::class
)
@OptionalValueDeserializer(
    deserializer = NBTTagConfig.OptionalValueDeserializer::class,
    delegateObjectDeserializer = true
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type", defaultImpl = NBTTagConfigCompound::class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["type"])
abstract class NBTTagConfig {
    @JsonIgnore
    protected val wolfyUtils: Scafall

    @get:JsonIgnore
    @set:JsonIgnore
    @JsonIgnore
    var parent: NBTTagConfig? = null

    protected constructor() {
        this.wolfyUtils = get()
    }

    protected constructor(parent: NBTTagConfig?) {
        this.wolfyUtils = get()
        this.parent = parent
    }

    abstract fun copy(): NBTTagConfig

    class OptionalValueDeserializer : ValueDeserializer<NBTTagConfig>(NBTTagConfig::class.java) {

        @Throws(IOException::class)
        override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): NBTTagConfig? {
            if (jsonParser.isExpectedStartObjectToken) {
                return null
            }
            val token = jsonParser.currentToken()
            var node: JsonNode? = null
            val regNBTQueries = ScafallRegistryTypes.nbtConfigs.resolveOrThrow()
            val type = when (token) {
                JsonToken.VALUE_STRING -> {
                    node = jsonParser.readValueAsTree()
                    val text = node.asText()
                    val matcher = NUM_PATTERN.matcher(text)
                    if (matcher.matches()) {
                        var id = matcher.group(2)
                        if (id != null) {
                            // integer value
                        } else {
                            // float value
                            id = matcher.group(4)
                        }
                        when (id!![0]) {
                            'b', 'B' -> regNBTQueries.getKey(NBTTagConfigByte::class.java)
                            's', 'S' -> regNBTQueries.getKey(NBTTagConfigShort::class.java)
                            'i', 'I' -> regNBTQueries.getKey(NBTTagConfigInt::class.java)
                            'l', 'L' -> regNBTQueries.getKey(NBTTagConfigLong::class.java)
                            'f', 'F' -> regNBTQueries.getKey(NBTTagConfigFloat::class.java)
                            'd', 'D' -> regNBTQueries.getKey(NBTTagConfigDouble::class.java)
                            else -> regNBTQueries.getKey(NBTTagConfigString::class.java)
                        }
                    }
                    regNBTQueries.getKey(NBTTagConfigString::class.java)
                }

                JsonToken.VALUE_NUMBER_INT -> regNBTQueries.getKey(NBTTagConfigInt::class.java)
                JsonToken.VALUE_NUMBER_FLOAT -> regNBTQueries.getKey(NBTTagConfigDouble::class.java)
                JsonToken.VALUE_FALSE, JsonToken.VALUE_TRUE -> regNBTQueries.getKey(
                    NBTTagConfigBoolean::class.java
                )

                else -> null
            }
            if (type == null) return null
            if (node == null) {
                node = jsonParser.readValueAsTree()
            }
            val objNode = ObjectNode(ctxt.nodeFactory)
            objNode.put("type", type.toString())
            objNode.set<JsonNode>("value", node)
            return ctxt.readTreeAsValue(objNode, NBTTagConfig::class.java)
        }

        companion object {
            private val NUM_PATTERN: Pattern = Pattern.compile("([0-9]+)([bBsSiIlL])|([0-9]?\\.?[0-9])+([fFdD])")
        }
    }

    companion object {
        private const val ERROR_MISMATCH = "Mismatched NBT types! Requested type: %s but found type %s, at node %s.%s"
    }
}
