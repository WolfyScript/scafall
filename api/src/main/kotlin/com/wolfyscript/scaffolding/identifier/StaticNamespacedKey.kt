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
package com.wolfyscript.scaffolding.identifier

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class StaticNamespacedKey(
    val value: String = "",
    /**
     * @return The namespace of the key
     */
    val namespace: String = "scaffolding",
    /**
     *
     * @return The key to the object
     */
    val key: String = ""
) {
    object KeyBuilder {
        fun createKeyString(annotated: Class<*>): String {
            val annotation = annotated.getAnnotation(StaticNamespacedKey::class.java)
            if (annotation != null) {
                if (annotation.value.isNotBlank()) {
                    return annotation.value
                }
                if (annotation.namespace.isNotBlank() && annotation.key.isNotBlank()) {
                    return annotation.namespace + ":" + annotation.key
                }
            }
            throw IllegalArgumentException("Invalid static id properties! Either use the value, or both the namespace and key options!")
        }
    }
}
