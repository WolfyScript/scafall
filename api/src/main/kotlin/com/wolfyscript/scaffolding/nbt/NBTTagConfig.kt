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

package com.wolfyscript.scaffolding.nbt;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wolfyscript.scaffolding.PluginWrapper;
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeIdResolver;
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeResolver;
import com.wolfyscript.scaffolding.config.jackson.OptionalValueDeserializer;
import com.wolfyscript.scaffolding.config.jackson.ValueDeserializer;
import com.wolfyscript.scaffolding.identifier.Key;
import com.wolfyscript.scaffolding.identifier.Keyed;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonTypeResolver(KeyedTypeResolver.class)
@JsonTypeIdResolver(KeyedTypeIdResolver.class)
@OptionalValueDeserializer(deserializer = NBTTagConfig.OptionalValueDeserializer.class, delegateObjectDeserializer = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type", defaultImpl = NBTTagConfigCompound.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = {"type"})
public abstract class NBTTagConfig implements Keyed {

    private static final String ERROR_MISMATCH = "Mismatched NBT types! Requested type: %s but found type %s, at node %s.%s";

    @JsonIgnore
    protected final PluginWrapper wolfyUtils;
    @JsonIgnore
    protected final Key type;
    @JsonIgnore
    protected NBTTagConfig parent;

    protected NBTTagConfig(@JacksonInject PluginWrapper wolfyUtils) {
        this.wolfyUtils = wolfyUtils;
        this.type = wolfyUtils.getIdentifiers().getNamespaced(getClass());
    }

    protected NBTTagConfig(PluginWrapper wolfyUtils, NBTTagConfig parent) {
        this.wolfyUtils = wolfyUtils;
        this.type = wolfyUtils.getIdentifiers().getNamespaced(getClass());
        this.parent = parent;
    }

    public Key getType() {
        return type;
    }

    @JsonIgnore
    @Override
    public Key key() {
        return type;
    }

    @JsonIgnore
    protected void setParent(NBTTagConfig parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public NBTTagConfig getParent() {
        return parent;
    }

    public abstract NBTTagConfig copy();

    public static class OptionalValueDeserializer extends ValueDeserializer<NBTTagConfig> {

        private static final Pattern NUM_PATTERN = Pattern.compile("([0-9]+)([bBsSiIlL])|([0-9]?\\.?[0-9])+([fFdD])");

        public OptionalValueDeserializer() {
            super(NBTTagConfig.class);
        }

        @Override
        public NBTTagConfig deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            if (jsonParser.isExpectedStartObjectToken()) {
                return null;
            }
            PluginWrapper wolfyUtils = (PluginWrapper) ctxt.findInjectableValue(PluginWrapper.class.getName(), null, null);
            var token = jsonParser.currentToken();
            JsonNode node = null;
            var regNBTQueries = wolfyUtils.getRegistries().getNbtTagConfigs();
            Key type = switch (token) {
                case VALUE_STRING -> {
                    node = jsonParser.readValueAsTree();
                    var text = node.asText();
                    Matcher matcher = NUM_PATTERN.matcher(text);
                    if (matcher.matches()) {
                        String id = matcher.group(2);
                        if (id != null) {
                            // integer value
                        } else {
                            // float value
                            id = matcher.group(4);
                        }
                        yield switch (id.charAt(0)) {
                            case 'b', 'B' -> regNBTQueries.getKey(NBTTagConfigByte.class);
                            case 's', 'S' -> regNBTQueries.getKey(NBTTagConfigShort.class);
                            case 'i', 'I' -> regNBTQueries.getKey(NBTTagConfigInt.class);
                            case 'l', 'L' -> regNBTQueries.getKey(NBTTagConfigLong.class);
                            case 'f', 'F' -> regNBTQueries.getKey(NBTTagConfigFloat.class);
                            case 'd', 'D' -> regNBTQueries.getKey(NBTTagConfigDouble.class);
                            default -> regNBTQueries.getKey(NBTTagConfigString.class);
                        };
                    }
                    yield regNBTQueries.getKey(NBTTagConfigString.class);
                }
                case VALUE_NUMBER_INT -> regNBTQueries.getKey(NBTTagConfigInt.class);
                case VALUE_NUMBER_FLOAT -> regNBTQueries.getKey(NBTTagConfigDouble.class);
                case VALUE_FALSE, VALUE_TRUE -> regNBTQueries.getKey(NBTTagConfigBoolean.class);
                default -> null;
            };
            if (type == null) return null;
            if (node == null) {
                node = jsonParser.readValueAsTree();
            }
            ObjectNode objNode = new ObjectNode(ctxt.getNodeFactory());
            objNode.put("type", type.toString());
            objNode.set("value", node);
            return ctxt.readTreeAsValue(objNode, NBTTagConfig.class);
        }
    }

}
