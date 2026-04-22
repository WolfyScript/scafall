package com.wolfyscript.scafall

import com.wolfyscript.scafall.loader.module.StandaloneInternalBootstrap

abstract class ScafallBootstrap(classloader: ClassLoader) :
   StandaloneInternalBootstrap<Scafall>(Scafall::class.java, classloader) {

    override val registered: Boolean
        get() = ScafallProvider.registered()

    override fun register(module: Scafall) {
        ScafallProvider.register(module)
    }

    override fun onCompleted(module: Scafall) {
        ScafallProvider.notifyListeners()
    }

}