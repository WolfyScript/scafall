package com.wolfyscript.scafall

import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.module.Module
import com.wolfyscript.scafall.loader.module.StandaloneInternalBootstrap

abstract class ScafallBootstrap(innerJarClassloader: ClassLoader) :
   StandaloneInternalBootstrap<Scafall>(ScafallModule::class.java, innerJarClassloader) {

       interface ScafallModule : Module<Scafall>
}