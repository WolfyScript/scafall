package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.core.ModIdentifier
import com.wolfyscript.scafall.maven.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.maven.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.registry.ScafallCommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.scheduler.SimpleScheduler
import com.wolfyscript.scafall.spigot.api.platform.SpigotPlatformManager
import com.wolfyscript.scafall.spigotlike.ScafallSpigotLike
import com.wolfyscript.scafall.spigotlike.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.spigotlike.api.identifier
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyLoader
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger

class ScafallSpigot(val classLoader: ClassLoader, val plugin: JavaPlugin, override val logger: Logger) : ScafallSpigotLike() {

    //
    // Note: This is called before scafall is registered! ScafallProvider.get() will fail!
    //       Only init things that don't depend on it and use init() instead!
    //

    override val identifier: ModIdentifier = plugin.identifier()

    // Essentials
    override val factories: SpigotFactoriesImpl = SpigotFactoriesImpl(this)
    override val registries: ScafallCommonRegistries = ScafallCommonRegistries()

    override val scheduler: SimpleScheduler = SimpleScheduler(logger)
    override val platformManager: SpigotPlatformManager = SpigotPlatformManager(this)

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler

    //
    // Spigot-only features
    //
    internal val pluginDependencyLoader = PluginDependencyLoader(this)

    override fun onInit() {
        // initiate essential components
        super.onInit()

        mavenDependencyHandler = MavenDependencyHandlerImpl(this, plugin.dataFolder.toPath().resolve("libs"))
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()
    }

    fun initServer(bukkitServer: Server) {
        server = ScafallServerSpigot(plugin, bukkitServer)
    }

    override fun getModIdentifier(id: String): ModIdentifier? {
        return Bukkit.getPluginManager().getPlugin(id)?.identifier()
    }

}