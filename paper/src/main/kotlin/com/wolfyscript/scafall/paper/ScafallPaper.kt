package com.wolfyscript.scafall.paper

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.common.api.registries.ScafallCommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.paper.api.PaperPlatformManager
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.server.ScafallServer
import com.wolfyscript.scafall.spigotlike.ScafallSpigotLike
import com.wolfyscript.scafall.spigotlike.api.BukkitPluginWrapper
import com.wolfyscript.scafall.spigotlike.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.spigotlike.api.scheduling.SchedulerImpl
import com.wolfyscript.scafall.spigotlike.api.wrappers.SpigotLikeWrapperUtilsImpl
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyLoader
import com.wolfyscript.scafall.wrappers.MinecraftWrapper
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

class ScafallPaper(val classLoader: ClassLoader, val plugin: JavaPlugin) : ScafallSpigotLike() {

    //
    // Note: This is called before this bridge is registered! ScafallProvider.get() will fail!
    //       Only init things that don't depend on it and use init() instead!
    //

    override val modInfo: ModWrapper = BukkitPluginWrapper(plugin)

    // Essentials
    override val factories: SpigotFactoriesImpl = SpigotFactoriesImpl(this)
    override val registries: ScafallCommonRegistries = ScafallCommonRegistries(this)

    override var server: ScafallServer? = null
    override val scheduler: Scheduler = SchedulerImpl()
    override val platformManager: PaperPlatformManager = PaperPlatformManager(this)
    override val minecraftWrapper: MinecraftWrapper = SpigotLikeWrapperUtilsImpl()

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
        server = ScafallServerPaper(this, bukkitServer)
    }

    override fun createOrGetPluginWrapper(pluginName: String): ModWrapper? {
        return Bukkit.getPluginManager().getPlugin(pluginName)?.let { BukkitPluginWrapper(it) }
    }

}
