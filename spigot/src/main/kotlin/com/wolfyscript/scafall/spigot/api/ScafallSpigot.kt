package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.ModWrapper
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallBootstrap
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
import com.wolfyscript.scafall.spigotlike.api.scheduling.SchedulerImpl
import com.wolfyscript.scafall.spigot.api.platform.SpigotPlatformManager
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
import com.wolfyscript.scafall.spigotlike.server.ScafallSpigotLikeServer
import com.wolfyscript.scafall.spigotlike.api.BukkitPluginWrapper
import com.wolfyscript.scafall.spigotlike.api.factories.SpigotFactoriesImpl
import com.wolfyscript.scafall.wrappers.MinecraftWrapper
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ScafallSpigot(val classLoader: ClassLoader, val plugin: JavaPlugin) : ScafallCommon(), ScafallBootstrap.ScafallModule {

    override val bridge: Scafall = this

    //
    // Note: This is called before this bridge is registered! ScafallProvider.get() will fail!
    //       Only init things that don't depend on it and use init() instead!
    //

    override val modInfo: ModWrapper = BukkitPluginWrapper(plugin)

    // Essentials
    override val factories: SpigotFactoriesImpl = SpigotFactoriesImpl(this)
    override val registries: ScafallCommonRegistries = ScafallCommonRegistries(this)

    override val server: ScafallServer = ScafallSpigotLikeServer()
    override val scheduler: Scheduler = SchedulerImpl()
    override val platformManager: SpigotPlatformManager = SpigotPlatformManager(this)
    override val minecraftWrapper: MinecraftWrapper = SpigotLikeWrapperUtilsImpl()
    override val adventure: SpigotAdventureUtil = SpigotAdventureUtil(this)

    override lateinit var mavenDependencyHandler: MavenDependencyHandler
    override lateinit var mavenRepositoryHandler: MavenRepositoryHandler

    //
    // Spigot-only features
    //
    internal val pluginDependencyLoader = PluginDependencyLoader(this)

    override fun onInit() {
        // initiate essential components
        factories.init()
        registries.initRegistries()
        registries.registerForJackson()

        ScafallRegistryTypes.dependencies.resolveOrThrow().apply {
            register(Key.defaultKey("plugins/${DenizenDependency.ID}"), DenizenDependency::class.java)
            register(Key.defaultKey("plugins/${EcoDependency.ID}"), EcoDependency::class.java)
            register(Key.defaultKey("plugins/${ExecutableItemsDependency.ID}"), ExecutableItemsDependency::class.java)
            register(Key.defaultKey("plugins/${ExecutableBlocksDependency.ID}"), ExecutableBlocksDependency::class.java)
            register(Key.defaultKey("plugins/${ItemsAdderDependency.ID}"), ItemsAdderDependency::class.java)
            register(Key.defaultKey("plugins/${MagicDependency.ID}"), MagicDependency::class.java)
            register(Key.defaultKey("plugins/${MMOItemsDependency.ID}"), MMOItemsDependency::class.java)
            register(Key.defaultKey("plugins/${MythicMobsDependency.ID}"), MythicMobsDependency::class.java)
            register(Key.defaultKey("plugins/${OraxenDependency.ID}"), OraxenDependency::class.java)
        }

        mavenDependencyHandler = MavenDependencyHandlerImpl(this, plugin.dataFolder.toPath().resolve("libs"))
        mavenRepositoryHandler = MavenRepositoryHandlerImpl()
    }

    /**
     * Initiates everything that requires that the plugin instance was created and other plugins are available, but doesn't require the plugin to be enabled.
     */
    override fun onLoad() {
        platformManager.implementationModules.forEach {
            it.value.onLoad()
        }
    }

    /**
     * initiates everything that requires the Spigot Plugin to be enabled.
     * e.g. Adventure, Events, etc.
     */
    override fun onEnable() {
        pluginDependencyLoader.loadDependencies()

        adventure.init()
        Bukkit.getPluginManager().registerEvents(pluginDependencyLoader, plugin)

        platformManager.implementationModules.forEach {
            it.value.onEnable()
        }
    }

    override fun onUnload() {
        platformManager.implementationModules.forEach {
            it.value.onUnload()
        }
        adventure.unload()
    }

    override fun createOrGetPluginWrapper(pluginName: String): ModWrapper? {
        return Bukkit.getPluginManager().getPlugin(pluginName)?.let { BukkitPluginWrapper(it) }
    }

}
