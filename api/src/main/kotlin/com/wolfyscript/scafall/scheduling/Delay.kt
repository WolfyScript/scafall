package com.wolfyscript.scafall.scheduling

interface Delay {

    companion object {

        fun amount(ticks: Long) : Amount {
            return TODO()
        }

    }

    object Instant : Delay

    interface Amount : Delay {

        val ticks: Long

    }

}