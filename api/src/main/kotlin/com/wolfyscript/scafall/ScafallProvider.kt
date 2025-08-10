package com.wolfyscript.scafall

import org.jetbrains.annotations.ApiStatus.Internal

interface ScafallProvider {

    companion object {
        private var instance: Scafall? = null
        private val listeners = mutableListOf<(Scafall)->Unit>()

        fun get() : Scafall {
            return instance ?: throw IllegalStateException("ScaffoldingProvider not initialized.")
        }

        fun registered() : Boolean {
            return instance != null
        }

        fun whenReady(listener: (Scafall)->Unit) {
            if (registered()) {
                listener(get())
            } else {
                listeners.add(listener)
            }
        }

        internal fun notifyListeners() {
            listeners.removeAll {
                it(instance!!)
                true
            }
        }

        @JvmSynthetic
        @Internal
        internal fun register(scafall: Scafall) {
            if (registered()) {
                throw IllegalStateException("ScaffoldingProvider already initialized.")
            }
            instance = scafall
        }

        @JvmSynthetic
        @Internal
        internal fun unregister(scafall: Scafall) {
            instance = null
        }

    }

}