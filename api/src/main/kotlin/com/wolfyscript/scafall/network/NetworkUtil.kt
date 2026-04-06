package com.wolfyscript.scafall.network

import com.wolfyscript.scafall.identifier.Key
import net.minecraft.network.protocol.Packet

interface NetworkUtil {

    fun <T: Packet<*>> registerPacketListener(id: Key, packetType: Class<T>, listener: (T) -> Unit)

    fun unregisterPacketListener(id: Key)

}

inline fun <reified T: Packet<*>> NetworkUtil.registerPacketListener(id: Key, noinline listener: (T) -> Unit) =
    registerPacketListener(id, T::class.java, listener)