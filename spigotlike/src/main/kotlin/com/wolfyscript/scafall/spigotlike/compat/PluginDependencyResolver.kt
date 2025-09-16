package com.wolfyscript.scafall.spigotlike.compat

import com.wolfyscript.scafall.compat.DependencyResolver
import com.wolfyscript.scafall.identifier.Key

class PluginDependencyResolver : DependencyResolver {

    override fun resolve(value: Any?, type: Class<*>?): Collection<Key> {
        if (type?.isAnnotationPresent(PluginDependencyResolverSettings::class.java) == true) {
            val anno = type.getAnnotation(PluginDependencyResolverSettings::class.java)
            if (anno != null) {
                val dependencyAnno = anno.dependencyType.java.getAnnotation(PluginDependency::class.java)
                if (dependencyAnno != null) {
                    val key = if (dependencyAnno.id.contains(":")) {
                        Key.parse(dependencyAnno.id)
                    } else {
                        Key.defaultKey(dependencyAnno.id)
                    }
                    return setOf(key)
                }
            }
        }
        return setOf()
    }
}
