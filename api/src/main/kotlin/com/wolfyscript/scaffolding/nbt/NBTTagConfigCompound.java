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
import com.wolfyscript.scaffolding.PluginWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@KeyedStaticId(key = "compound")
public class NBTTagConfigCompound extends NBTTagConfig {

    @JsonIgnore
    protected Map<String, NBTTagConfig> children;

    @JsonCreator
    NBTTagConfigCompound(@JacksonInject PluginWrapper wolfyUtils) {
        super(wolfyUtils);
        this.children = new HashMap<>();
    }

    public NBTTagConfigCompound(PluginWrapper wolfyUtils, NBTTagConfig parent) {
        super(wolfyUtils, parent);
        this.children = new HashMap<>();
    }

    protected NBTTagConfigCompound(NBTTagConfigCompound other) {
        super(other.wolfyUtils);
        this.children = other.children.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            NBTTagConfig value = entry.getValue().copy();
            value.setParent(this);
            return value;
        }));
    }

    @JsonAnySetter
    public void loadNonNestedChildren(String key, NBTTagConfig child) {
        //Sets the children that are specified in the root of the object without the "children" node!
        //That is supported behaviour!
        children.putIfAbsent(key, child);
        child.setParent(this);
    }

    @JsonSetter("children")
    public void setChildren(Map<String, NBTTagConfig> children) {
        this.children = children.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            NBTTagConfig value = entry.getValue();
            value.setParent(this);
            return value;
        }));
    }

    @JsonGetter
    public Map<String, NBTTagConfig> getChildren() {
        return children;
    }

    @Override
    public NBTTagConfigCompound copy() {
        return new NBTTagConfigCompound(this);
    }

}
