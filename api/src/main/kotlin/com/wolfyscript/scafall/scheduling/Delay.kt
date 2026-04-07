package com.wolfyscript.scafall.scheduling

interface Delay {

    companion object {

        fun amount(ticks: Int) : Amount {
            return DelayAmount(ticks)
        }

    }

    val ticks: Int

    object Instant : Delay {
        override val ticks: Int = 0
    }

    interface Amount : Delay

}