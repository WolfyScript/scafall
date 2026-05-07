package com.wolfyscript.scafall

import org.jetbrains.annotations.ApiStatus.Internal

/**
 * Provides a global instance of the Scafall class and allows registering listeners that will be called when the Scafall instance is ready.
 */
interface ScafallProvider {

    companion object {
        private var instance: Scafall? = null
        private val listeners = mutableListOf<(Scafall) -> Unit>()

        /**
         * Returns the instance of the Scafall provider.
         *
         * @throws IllegalStateException if the provider is not initialized.
         * @return The instance of the Scafall provider
         */
        fun get() : Scafall {
            return instance ?: throw IllegalStateException("ScaffoldingProvider not initialized.")
        }

        /**
         * Checks if the Scafall provider is already initialized.
         *
         * @return true if the provider is initialized, false otherwise
         */
        fun registered() : Boolean {
            return instance != null
        }

        /**
         * Registers a listener that will be called when the Scafall provider is initialized.
         * If scafall is already initialized, the listener will be called immediately.
         *
         * @param listener The listener to be called when scafall is initialized
         */
        fun whenReady(listener: (Scafall) -> Unit) {
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