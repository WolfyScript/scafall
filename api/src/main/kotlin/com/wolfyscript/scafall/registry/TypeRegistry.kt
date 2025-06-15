package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.identifier.Key

/**
 * This registry allows you to register classes under NamespacedKeys. <br></br>
 * It is similar to the [Registry], with the difference that it stores the classes of the type. <br></br>
 * To get a new instance of an entry you must use [.create] or create it manually. <br></br>
 *
 *
 * Main use case of this registry would be to prevent using the [Registry] with default objects <br></br>
 * and prevents unwanted usage of those values, as this registry enforces to create new instances.
 *
 * @param <V> The type of the values.
</V> */
interface TypeRegistry<V> : Registry<Class<out V>> {

    /**
     * This method creates a new instance of the specific class, if it is available. <br></br>
     * If class for the key is not found it will return null. <br></br>
     * Default implementation looks for the default constructor.
     *
     * @param key The [NamespacedKey] of the value.
     * @return A new instance of the class.
     */
    fun create(key: Key): V?

}
