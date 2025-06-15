package com.wolfyscript.scafall.data

interface DataHolder<H : DataHolder<H, M>, M: DataComponentMap<H>> {

    val data: M

    fun <T : Any> get(key: DataKey<T, in H>): T? = data.get(key)

    interface Mutable<H : Mutable<H>> : DataHolder<H, DataComponentMap.Mutable<H>> {

        /**
         * Sets the value of the specified Key
         */
        fun <T : Any> set(key: DataKey<T, in H>, data: T) = this.data.set(key, data)

        fun <T: Any> remove(key: DataKey<T, in H>) = this.data.remove(key)

    }

    interface Immutable<H : Immutable<H>> : DataHolder<H, DataComponentMap.Immutable<H>> {

        /**
         * Sets the value of the specified Key
         */
        fun <T : Any> set(key: DataKey<T, in H>, data: T) : H = this.data.set(key, data)

    }

}
