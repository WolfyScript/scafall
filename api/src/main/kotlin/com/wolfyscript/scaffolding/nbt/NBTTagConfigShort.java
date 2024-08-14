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

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wolfyscript.utilities.KeyedStaticId;
import com.wolfyscript.utilities.WolfyUtils;
import com.wolfyscript.utilities.config.jackson.OptionalValueSerializer;
import com.wolfyscript.utilities.config.jackson.ValueSerializer;
import com.wolfyscript.utilities.eval.value_provider.ValueProvider;
import com.wolfyscript.utilities.eval.value_provider.ValueProviderShortConst;

import java.io.IOException;

@OptionalValueSerializer(serializer = NBTTagConfigShort.OptionalValueProvider.class)
@KeyedStaticId(key = "short")
public class NBTTagConfigShort extends NBTTagConfigPrimitive<Short> {

    @JsonCreator
    NBTTagConfigShort(@JacksonInject WolfyUtils wolfyUtils, @JsonProperty("value") ValueProvider<Short> value) {
        super(wolfyUtils, value);
    }

    public NBTTagConfigShort(WolfyUtils wolfyUtils, NBTTagConfig parent, ValueProvider<Short> value) {
        super(wolfyUtils, parent, value);
    }

    public NBTTagConfigShort(NBTTagConfigPrimitive<Short> other) {
        super(other);
    }

    @Override
    public NBTTagConfigShort copy() {
        return new NBTTagConfigShort(this);
    }

    public static class OptionalValueProvider extends ValueSerializer<NBTTagConfigShort> {

        public OptionalValueProvider() {
            super(NBTTagConfigShort.class);
        }

        @Override
        public boolean serialize(NBTTagConfigShort targetObject, JsonGenerator generator, SerializerProvider provider) throws IOException {
            if (targetObject.value instanceof ValueProviderShortConst shortConst) {
                generator.writeObject(shortConst);
                return true;
            }
            return false;
        }
    }
}
