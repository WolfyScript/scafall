package com.wolfyscript.scafall.fabric

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.fabric.api.ScafallFabricServer
import com.wolfyscript.scafall.loader.ScafallLoader.loadObject
import com.wolfyscript.scafall.loader.module.Module
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ScafallFabricMod : ModInitializer {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private var serverModule: Module<Scafall>? = null

    override fun onInitialize() {
        val bootstrap = loadObject(
            ScafallBootstrap::class.java,
            javaClass.classLoader,
            "com.wolfyscript.scafall.InternalBootstrap"
        )

        ServerLifecycleEvents.SERVER_STARTING.register {
            logger.info("ScafallFabricMod server starting")
            serverModule = bootstrap.loadModule {
                ScafallFabricServer(javaClass.classLoader, it, logger)
            }
            serverModule?.onLoad()
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            logger.info("ScafallFabricMod server startet")
            ScafallProvider.whenReady {
                serverModule?.onEnable()
            }
        }

        ServerLifecycleEvents.SERVER_STOPPED.register {
            ScafallProvider.whenReady {
                serverModule?.onUnload()
            }
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, env ->



        }

    }

}