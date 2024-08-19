package com.wolfyscript.scafall

import org.jetbrains.annotations.ApiStatus.Internal

interface ScafallProvider {

    companion object {
        private var instance: Scafall? = null

        fun get() : Scafall {
            return instance ?: throw IllegalStateException("ScaffoldingProvider not initialized.")
        }

        fun registered() : Boolean {
            return instance != null
        }

        @Internal
        internal fun register(scafall: Scafall) {
            if (registered()) {
                throw IllegalStateException("ScaffoldingProvider already initialized.")
            }
            instance = scafall
        }

        @Internal
        internal fun unregister(scafall: Scafall) {
            instance = null
        }

    }

}