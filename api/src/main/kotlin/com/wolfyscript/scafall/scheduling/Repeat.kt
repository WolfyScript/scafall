package com.wolfyscript.scafall.scheduling

sealed interface Repeat {

    companion object {

        fun forever(tickInterval: Long): Forever {
            return TODO()
        }

        fun amount(amount: Int, tickInterval: Long): Amount {
            return TODO()
        }

    }

    /**
     * Updates the repeat state.
     */
    fun tick()

    /**
     * Repeat is completed when the condition of the type is met.
     */
    val completed: Boolean

    /**
     *
     */
    object Never : Repeat {
        override fun tick() {}
        override val completed: Boolean = true
    }

    interface Amount : Repeat {

        val amount: Int

        val interval: Long

    }

    interface Forever : Repeat {

        val interval: Long

    }

}