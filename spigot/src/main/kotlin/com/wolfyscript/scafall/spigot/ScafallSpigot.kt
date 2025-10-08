package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.common.api.ScafallCommon
import com.wolfyscript.scafall.common.api.dependencies.MavenDependencyHandlerImpl
import com.wolfyscript.scafall.common.api.dependencies.MavenRepositoryHandlerImpl
import com.wolfyscript.scafall.common.api.registries.ScafallCommonRegistries
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.maven.MavenDependencyHandler
import com.wolfyscript.scafall.maven.MavenRepositoryHandler
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.scheduling.Scheduler
import com.wolfyscript.scafall.server.ScafallServer
import com.wolfyscript.scafall.spigot.api.platform.SpigotPlatformManager
import com.wolfyscript.scafall.spigotlike.ScafallSpigotLike
import com.wolfyscript.scafall.spigotlike.api.BukkitPluginWrapper
import com.wolfyscript.scafall.spigotlike.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.spigotlike.api.scheduling.SchedulerImpl
import com.wolfyscript.scafall.spigotlike.api.wrappers.SpigotLikeWrapperUtilsImpl
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyLoader
import com.wolfyscript.scafall.spigotlike.compat.denizen.DenizenDependency
import com.wolfyscript.scafall.spigotlike.compat.eco.EcoDependency
import com.wolfyscript.scafall.spigotlike.compat.executableblocks.ExecutableBlocksDependency
import com.wolfyscript.scafall.spigotlike.compat.executableitems.ExecutableItemsDependency
import com.wolfyscript.scafall.spigotlike.compat.itemsadder.ItemsAdderDependency
import com.wolfyscript.scafall.spigotlike.compat.magic.MagicDependency
import com.wolfyscript.scafall.spigotlike.compat.mmoitems.MMOItemsDependency
import com.wolfyscript.scafall.spigotlike.compat.mythicmobs.MythicMobsDependency
import com.wolfyscript.scafall.spigotlike.compat.oraxen.OraxenDependency
import com.wolfyscript.scafall.wrappers.MinecraftWrapper
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
    override val registries: ScafallCommonRegistries = ScafallCommonRegistries(this)

    override var server: ScafallServer? = null
    override val scheduler: Scheduler = SchedulerImpl()
    override val platformManager: SpigotPlatformManager = SpigotPlatformManager(this)
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
        server = ScafallServerSpigot(plugin, bukkitServer)
    }

    override fun createOrGetPluginWrapper(pluginName: String): ModWrapper? {
        return Bukkit.getPluginManager().getPlugin(pluginName)?.let { BukkitPluginWrapper(it) }
    }

}