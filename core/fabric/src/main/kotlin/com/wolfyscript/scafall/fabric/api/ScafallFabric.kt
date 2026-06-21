package com.wolfyscript.scafall.fabric.api

import com.wolfyscript.scafall.ScafallCommon
import com.wolfyscript.scafall.maven.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.maven.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.registry.ScafallCommonRegistries
import com.wolfyscript.scafall.fabric.api.factories.FabricFactoriesImpl
import com.wolfyscript.scafall.fabric.api.platform.FabricPlatformManager
import com.wolfyscript.scafall.fabric.server.FabricScafallServer
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.scheduler.SimpleScheduler
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import org.slf4j.Logger
import java.io.File

/**
 * Scafall is currently only supposed to work on the server.
 * T
 *
 */
class ScafallFabric(val classLoader: ClassLoader, override val logger: Logger) : ScafallCommon() {

    //
    // Note: This is called before this bridge is registered! ScafallProvider.get() will fail!
    //       Only init things that don't depend on it and use init() instead!
    //

    override val registries: ScafallCommonRegistries = ScafallCommonRegistries()
    override val scheduler: SimpleScheduler = SimpleScheduler(logger)
    override val platformManager: PlatformManager = FabricPlatformManager(this)
    override val factories: FabricFactoriesImpl = FabricFactoriesImpl(this)

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler

    override fun onInit() {
        logger.info("[scafall] Initializing...")
        factories.init()

        ServerTickEvents.START_SERVER_TICK.register { server ->
            scheduler.tick(server.tickCount)
        }

        registries.initRegistries()
        registries.registerForJackson()

        // TODO
        mavenDependencyHandler = MavenDependencyHandlerImpl(this, File("./libs").toPath())
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()
    }

    fun initServer(minecraftServer: MinecraftServer) {
        server = FabricScafallServer(minecraftServer)
    }

}