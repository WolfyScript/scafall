package com.wolfyscript.scafall.scheduling

import com.wolfyscript.scafall.ModWrapper

interface Scheduler {

    fun async(plugin: ModWrapper, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: suspend () -> Unit): Task

    fun sync(plugin: ModWrapper, delay: Delay = Delay.Instant, timer: Timer = Timer.Once, task: () -> Unit): Task
    
}
