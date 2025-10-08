package com.wolfyscript.scafall

import org.jetbrains.annotations.ApiStatus.Internal

/**
 * Creates internal bootstrap classes that are linked to this API module.
 *
 */
@Internal
internal class InternalBootstrap(val classLoader: ClassLoader) : ScafallBootstrap(classLoader) {

    override val registered: Boolean
        get() = ScafallProvider.registered()

    override fun register(module: Scafall) {
        ScafallProvider.register(module)
    }

    override fun onCompleted(module: Scafall) {
        ScafallProvider.notifyListeners()
    }

}