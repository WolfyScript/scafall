package com.wolfyscript.scafall

import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.module.Module
import org.jetbrains.annotations.ApiStatus.Internal

/**
 * Creates internal bootstrap classes that are linked to this API module.
 *
 */
@Internal
internal class InternalBootstrap(val classLoader: ClassLoader) : ScafallBootstrap(classLoader) {

    override val registered: Boolean
        get() = ScafallProvider.registered()

    override fun register(module: Module<Scafall>) {
        ScafallProvider.register(module.bridge)
    }

}