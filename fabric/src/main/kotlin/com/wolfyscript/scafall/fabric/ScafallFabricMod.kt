package com.wolfyscript.scafall.fabric

import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.fabric.api.ScafallFabric
import com.wolfyscript.scafall.loader.ScafallLoader.loadObject
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ScafallFabricMod : ModInitializer {

    val bootstrap = loadObject(
        ScafallBootstrap::class.java,
        javaClass.classLoader,
        "com.wolfyscript.scafall.InternalBootstrap"
    )

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private var scafall: ScafallFabric = bootstrap.loadModule {
        ScafallFabric(javaClass.classLoader, logger)
    }

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            logger.info("ScafallFabricMod server starting")

            scafall.initServer(it)
            scafall.server?.onLoad()
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            logger.info("ScafallFabricMod server startet")
        }

        ServerLifecycleEvents.SERVER_STOPPED.register {

            scafall.server?.onUnload()
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, env ->



        }

    }

}