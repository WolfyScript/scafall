package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.maven.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.maven.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.registry.ScafallCommonRegistries
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.scheduling.SimpleScheduler
import com.wolfyscript.scafall.spigot.api.platform.SpigotPlatformManager
import com.wolfyscript.scafall.spigotlike.ScafallSpigotLike
import com.wolfyscript.scafall.spigotlike.api.BukkitPluginWrapper
import com.wolfyscript.scafall.spigotlike.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyLoader
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

class ScafallSpigot(val classLoader: ClassLoader, val plugin: JavaPlugin) : ScafallSpigotLike() {

    //
    // Note: This is called before scafall is registered! ScafallProvider.get() will fail!
    //       Only init things that don't depend on it and use init() instead!
    //

    override val modInfo: ModWrapper = BukkitPluginWrapper(plugin)

    // Essentials
    override val factories: SpigotFactoriesImpl = SpigotFactoriesImpl(this)
    override val registries: ScafallCommonRegistries = ScafallCommonRegistries()

    override val scheduler: SimpleScheduler = SimpleScheduler()
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

    override fun createOrGetPluginWrapper(modName: String): ModWrapper? {
        return Bukkit.getPluginManager().getPlugin(modName)?.let { BukkitPluginWrapper(it) }
    }

}