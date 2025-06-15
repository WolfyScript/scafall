package com.wolfyscript.scafall.registry

import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.function.Supplier

abstract class AbstractTypeRegistry<M : MutableMap<Key, Class<out V>>, V>(
    override val key: Key,
    protected val map: M
) : TypeRegistry<V> {

    constructor(key: Key, mapSupplier: Supplier<M>) : this(
        key,
        mapSupplier.get(),
    )

    override fun get(key: Key): Class<out V>? {
        return map[key]
    }

    override fun create(key: Key): V? {
        val clazz = get(key)
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun register(key: Key, value: Class<out V>) {
        Preconditions.checkState(!map.containsKey(key), "namespaced key '%s' already has an associated value!", key)
        map.put(key, value)
    }

    override fun iterator(): Iterator<Class<out V>> {
        return map.values.iterator()
    }

    override fun keySet(): Set<Key> {
        return Collections.unmodifiableSet(map.keys)
    }

    override fun values(): Collection<Class<out V>> {
        return Collections.unmodifiableCollection(map.values)
    }

    override fun entrySet(): Set<Map.Entry<Key, Class<out V>>> {
        return Collections.unmodifiableSet<Map.Entry<Key, Class<out V>>>(map.entries)
    }

}
