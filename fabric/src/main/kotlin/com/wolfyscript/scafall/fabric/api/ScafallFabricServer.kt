package com.wolfyscript.scafall.fabric.api

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.common.api.ScafallCommon
import com.wolfyscript.scafall.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.common.api.registries.ScafallCommonRegistries
import com.wolfyscript.scafall.fabric.api.factories.FabricFactoriesImpl
import com.wolfyscript.scafall.fabric.api.platform.FabricPlatformManager
import com.wolfyscript.scafall.fabric.api.wrappers.FabricModWrapper
import com.wolfyscript.scafall.fabric.api.wrappers.FabricWrapperUtils
import com.wolfyscript.scafall.fabric.server.FabricScafallServer
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.platform.PlatformManager
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.server.ScafallServer
import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import org.slf4j.Logger
import java.io.File

/**
 * Scafall is currently only supposed to work on the server.
 * T
 *
 */
class ScafallFabricServer(val classLoader: ClassLoader, val mcServer: MinecraftServer, override val logger: Logger) : ScafallCommon(), ScafallBootstrap.ScafallModule {

    override val bridge: Scafall = this

    //
    // Note: This is called before this bridge is registered! ScafallProvider.get() will fail!
    //       Only init things that don't depend on it and use init() instead!
    //

    override val server: ScafallServer = FabricScafallServer(mcServer)
    override val registries: ScafallCommonRegistries = ScafallCommonRegistries(this)
    override val scheduler: Scheduler
        get() = TODO("Not yet implemented")
    override val platformManager: PlatformManager = FabricPlatformManager(this)
    override val factories: FabricFactoriesImpl = FabricFactoriesImpl(this)
    override val modInfo: ModWrapper = FabricModWrapper(FabricLoader.getInstance().getModContainer("scafall").get(), logger)
    override val adventure: AdventureUtil = FabricAdventureUtil(this)
    override val minecraftWrapper: MinecraftWrapper = FabricWrapperUtils()

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler

    override fun onInit() {
        logger.info("Initializing Scafall")
        factories.init()

        registries.initRegistries()
        registries.registerForJackson()

        // TODO
        mavenDependencyHandler = MavenDependencyHandlerImpl(this, File("./libs").toPath())
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()
    }

    override fun onLoad() {

    }

    override fun onEnable() {

    }

    override fun onUnload() {

    }

    override fun createOrGetPluginWrapper(pluginName: String): ModWrapper? {
        return null // TODO
    }

}