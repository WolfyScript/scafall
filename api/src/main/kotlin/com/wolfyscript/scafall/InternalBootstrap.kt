package com.wolfyscript.scafall

import org.jetbrains.annotations.ApiStatus.Internal

/**
 * Creates internal bootstrap classes that are linked to this API module.
 *
 */
@Internal
internal class InternalBootstrap(val classLoader: ClassLoader) : ScafallBootstrap(classLoader)