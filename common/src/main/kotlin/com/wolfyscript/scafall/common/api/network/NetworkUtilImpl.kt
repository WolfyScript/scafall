package com.wolfyscript.scafall.common.api.network

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.network.NetworkUtil
import net.minecraft.network.protocol.Packet

class NetworkUtilImpl : NetworkUtil {

    private val byKey = mutableMapOf<Key, PacketListener<*>>()
    private val byPacketType = mutableMapOf<Class<out Packet<*>>, MutableSet<Key>>()

    override fun <T: Packet<*>> registerPacketListener(
        id: Key,
        packetType: Class<T>,
        listener: (T) -> Unit,
    ) {
        require(!byKey.containsKey(id)) {
            "A packet listener with id $id is already registered!"
        }
        byKey[id] = PacketListener(id, listener)
        byPacketType.getOrPut(packetType, ::mutableSetOf).add(id)
    }

    override fun unregisterPacketListener(id: Key) {
        byKey.remove(id)
        for (keys in byPacketType.values) {
            keys.remove(id)
        }
    }

    fun callListenersFor(packet: Packet<*>) {


    }


    private data class PacketListener<T: Packet<*>>(
        val key: Key,
        val listener: (T) -> Unit
    ) {

        fun call(packet: T) = listener(packet)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is PacketListener<*>) return false

            if (key != other.key) return false

            return true
        }

        override fun hashCode(): Int {
            return key.hashCode()
        }

    }

}