package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.*
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.util.stream.Collectors

@StaticNamespacedKey(key = "compound")
class NBTTagConfigCompound : NBTTagConfig {

    @JsonIgnore
    var children: MutableMap<String, NBTTagConfig> = HashMap()
        set(value) {
            field = value.entries.stream().collect(
                Collectors.toMap(
                    { it.key },
                    { entry: Map.Entry<String, NBTTagConfig> ->
                        val value = entry.value
                        value.parent = this
                        value
                    })
            )
        }

    @JsonCreator
    internal constructor() : super() {
        this.children = HashMap()
    }

    constructor(parent: NBTTagConfig?) : super(parent) {
        this.children = HashMap()
    }

    private constructor(other: NBTTagConfigCompound) : super() {
        this.children = other.children
    }

    @JsonAnySetter
    fun loadNonNestedChildren(key: String, child: NBTTagConfig) {
        //Sets the children that are specified in the root of the object without the "children" node!
        //That is supported behaviour!
        children.putIfAbsent(key, child)
        child.parent = this
    }

    @JsonSetter("children")
    private fun setJsonChildren(children: MutableMap<String, NBTTagConfig>) {
        this.children = children
    }

    @JsonGetter
    private fun getJsonChildren(): Map<String, NBTTagConfig> {
        return children
    }

    override fun copy(): NBTTagConfigCompound {
        return NBTTagConfigCompound(this)
    }
}
