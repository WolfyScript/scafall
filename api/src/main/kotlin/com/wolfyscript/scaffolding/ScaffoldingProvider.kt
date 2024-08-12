package com.wolfyscript.scaffolding

import org.jetbrains.annotations.ApiStatus.Internal

internal interface ScaffoldingProvider {

    companion object {
        private var instance: Scaffolding? = null

        fun get() : Scaffolding {
            return instance ?: throw IllegalStateException("ScaffoldingProvider not initialized.")
        }

        fun registered() : Boolean {
            return instance != null
        }

        @Internal
        internal fun register(scaffolding: Scaffolding) {
            if (registered()) {
                throw IllegalStateException("ScaffoldingProvider already initialized.")
            }
            instance = scaffolding
        }

        @Internal
        internal fun unregister(scaffolding: Scaffolding) {
            instance = null
        }

    }

}