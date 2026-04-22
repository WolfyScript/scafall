package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper

interface Task {

    fun cancel()

    fun plugin(): ModWrapper

}
