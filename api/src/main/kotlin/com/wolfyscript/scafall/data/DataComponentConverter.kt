package com.wolfyscript.scafall.data

import com.wolfyscript.scafall.identifier.Key
import kotlin.reflect.KClass

/**
 * A converter that converts data from a DataHolder into a specified type or writes that type of data to the holder.
 *
 * * [T] The type of data to convert to
 * * [B] The base DataHolder that data can be read from
 * * [M] The mutable DataHolder that data can be written to
 */
interface DataComponentConverter<T : Any, B: DataHolder<*,*>, M: DataHolder<M, *>> {

    val key: Key

    val type: KClass<T>

    val reader: Reader<T, B>
    val modifier: Modifier<T, M>

    /**
     * The Reader used to read the data from the base DataHolder
     */
    interface Reader<T: Any, H: DataHolder<*,*>> {
        val converter: H.() -> Result<T?>
    }

    /**
     * The writer used to write data to the mutable DataHolder
     */
    interface Modifier<T: Any, H: DataHolder<H,*>> {
        val converter: H.(T) -> Result<H>
        val remover: H.() -> Result<Pair<H, Boolean>>
    }

}

