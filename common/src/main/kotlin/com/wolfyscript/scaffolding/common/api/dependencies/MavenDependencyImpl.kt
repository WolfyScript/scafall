package com.wolfyscript.scaffolding.common.api.dependencies

import com.wolfyscript.scaffolding.maven.MavenDependency

class MavenDependencyImpl(
    override val group: String,
    override val name: String,
    override val version: String
) : MavenDependency {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MavenDependencyImpl

        if (group != other.group) return false
        if (name != other.name) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = group.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + version.hashCode()
        return result
    }
    
}