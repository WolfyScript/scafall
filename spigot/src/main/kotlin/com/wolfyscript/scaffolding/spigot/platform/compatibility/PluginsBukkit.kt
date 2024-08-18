/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scaffolding.spigot.platform.compatibility

import com.google.common.base.Preconditions
import com.google.inject.*
import com.google.inject.name.Names
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.into
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

/**
 * Manages compatibility with other plugins. <br></br>
 * It will load plugin specific integrations that are initialised if the corresponding plugins are enabled.
 */
internal class PluginsBukkit(private val core: Scaffolding) : Plugins,
    Listener {
    private val pluginAdapters: MutableMap<String, MutableMap<Key, PluginAdapter>> = HashMap()
    private val pluginIntegrationsMap: MutableMap<String, PluginIntegrationAbstract?> = HashMap()
    private val pluginIntegrationClasses: MutableMap<String, Class<out PluginIntegrationAbstract>> = HashMap()
    private var doneLoading = false

    override val isDoneLoading: Boolean
        get() = doneLoading

    override val pluginIntegrations: Collection<PluginIntegration>
        get() = Collections.unmodifiableCollection<PluginIntegration>(pluginIntegrationsMap.values)

    /**
     * Looks for available PluginIntegrations and loads them.<br></br>
     * <br></br>
     * The PluginIntegrations, for that the corresponding plugins are already enabled at this point, will be initiated.<br></br>
     * In case a corresponding plugin is disabled, the loaded class will be removed and the integration won't be initiated!
     * This applies to plugins that are disabled afterwards too!
     * <br></br>
     * <br></br>
     * Other PluginIntegrations will be initiated when the corresponding plugins are enabled, or if they are loading data async, when they completely loaded that data.<br></br>
     * <br></br>
     * To check if a PluginIntegration is enabled you need to use the [PluginIntegration.isDoneLoading].<br></br>
     */
    fun init() {
        core.logger.info("Loading Plugin integrations: ")
        Bukkit.getPluginManager().registerEvents(this, core.corePlugin.into().plugin)
        for (integrationType in core.reflections.getTypesAnnotatedWith(WUPluginIntegration::class.java)) {
            val annotation = integrationType.getAnnotation(
                WUPluginIntegration::class.java
            )
            if (annotation != null && PluginIntegrationAbstract::class.java.isAssignableFrom(integrationType)) {
                val pluginName = annotation.pluginName
                if (Bukkit.getPluginManager().getPlugin(pluginName) != null) { //Only load for plugins that are loaded.
                    core.logger.info(" - {}", pluginName)
                    Preconditions.checkArgument(
                        !pluginIntegrationClasses.containsKey(pluginName),
                        "Failed to add Integration! A Plugin Integration for \"$pluginName\" already exists!"
                    )
                    pluginIntegrationClasses[pluginName] = integrationType as Class<out PluginIntegrationAbstract>
                }
            }
        }
        if (!pluginIntegrationClasses.isEmpty()) {
            core.logger.info("Create & Init Plugin integrations: ")
            //Initialize the plugin integrations for that the plugin is already enabled.
            pluginIntegrationClasses.forEach { (pluginName: String?, integrationClass: Class<out PluginIntegrationAbstract?>?) ->
                this.createOrInitPluginIntegration(
                    pluginName,
                    integrationClass
                )
            }
            if (pluginIntegrationsMap.isEmpty()) {
                core.logger.info(" - No integrations created.")
            }
        } else {
            core.logger.info(" - No integrations found")
            doneLoading = true
        }
    }

    private fun createOrInitPluginIntegration(
        pluginName: String,
        integrationClass: Class<out PluginIntegrationAbstract>
    ) {
        val integration = pluginIntegrationsMap.computeIfAbsent(pluginName) { _ ->
            try {
                val injector = Guice.createInjector(Module { binder: Binder ->
                    binder.bindConstant().annotatedWith(Names.named("pluginName")).to(pluginName)
                    binder.bind(Scaffolding::class.java).toInstance(
                        core
                    )
                })
                return@computeIfAbsent injector.getInstance(integrationClass)
            } catch (e: ConfigurationException) {
                core.logger.warn("     Failed to initialise integration for {}! Cause: {}", pluginName, e.message)
                return@computeIfAbsent null
            } catch (e: ProvisionException) {
                core.logger.warn("     Failed to initialise integration for {}! Cause: {}", pluginName, e.message)
                return@computeIfAbsent null
            } catch (e: CreationException) {
                core.logger.warn("     Failed to initialise integration for {}! Cause: {}", pluginName, e.message)
                return@computeIfAbsent null
            }
        }
        if (integration == null) return
        val plugin = Bukkit.getPluginManager().getPlugin(pluginName)
        if (plugin != null && plugin.isEnabled && !integration.isDoneLoading) {
            integration.init(plugin)
            if (!integration.hasAsyncLoading()) {
                integration.enable()
            }
            checkDependencies()
        }
    }

    private fun ignoreIntegrationFor(plugin: Plugin) {
        runIfAvailable(
            plugin.name,
            PluginIntegrationAbstract::class.java
        ) { obj: PluginIntegrationAbstract -> obj.ignore() }
        checkDependencies()
    }

    fun checkDependencies() {
        pluginIntegrationsMap.values.removeIf { pluginIntegrationAbstract: PluginIntegrationAbstract? ->
            if (pluginIntegrationAbstract!!.shouldBeIgnored()) {
                pluginIntegrationClasses.remove(pluginIntegrationAbstract.associatedPlugin)
                return@removeIf true
            }
            false
        }
        val availableIntegrations = pluginIntegrationClasses.size
        val enabledIntegrations =
            pluginIntegrationsMap.values.stream().filter { obj: PluginIntegrationAbstract? -> obj!!.isDoneLoading }.count()
        if (availableIntegrations.toLong() == enabledIntegrations) {
            doneLoading = true
            Bukkit.getScheduler().runTaskLater(core.corePlugin.into().plugin, Runnable {
                core.logger.info("Dependencies Loaded. Calling DependenciesLoadedEvent!")
                Bukkit.getPluginManager().callEvent(DependenciesLoadedEvent(core))
            }, 2)
        }
    }

    @EventHandler
    private fun onPluginDisable(event: PluginDisableEvent) {
        ignoreIntegrationFor(event.plugin)
    }

    @EventHandler
    private fun onPluginEnable(event: PluginEnableEvent) {
        val pluginName = event.plugin.name
        val integrationClass = pluginIntegrationClasses[pluginName]
        if (integrationClass != null) {
            createOrInitPluginIntegration(pluginName, integrationClass)
            if (!hasIntegration(event.plugin.name)) {
                core.logger.warn("Failed to initiate PluginIntegration for {}", pluginName)
                ignoreIntegrationFor(event.plugin)
            }
        }
    }

    /**
     * @param pluginName The name of the plugin to check for
     * @return If the plugin is loaded
     */
    override fun isPluginEnabled(pluginName: String): Boolean {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName)
    }

    override fun hasWorldGuard(): Boolean {
        return isPluginEnabled("WorldGuard")
    }

    override fun hasPlotSquared(): Boolean {
        return isPluginEnabled("PlotSquared")
    }

    override fun hasLWC(): Boolean {
        return isPluginEnabled("LWC")
    }

    override fun hasPlaceHolderAPI(): Boolean {
        return isPluginEnabled("PlaceholderAPI")
    }

    override fun hasMcMMO(): Boolean {
        return isPluginEnabled("mcMMO")
    }

    override fun hasIntegration(pluginName: String): Boolean {
        return pluginIntegrationsMap.containsKey(pluginName)
    }

    /**
     * Gets the integration of the specified plugin and type.<br></br>
     * In case there is no integration available, it returns null.<br></br>
     *
     * @param pluginName The plugin name to get the integration for.
     * @return The integration from the plugin; null if not available.
     */
    override fun getIntegration(pluginName: String): PluginIntegration? {
        return pluginIntegrationsMap[pluginName]
    }

    /**
     * Gets the integration of the specified plugin and type.<br></br>
     * In case there is no integration available, it returns null.<br></br>
     * If it does exist it will try to cast the integration to the specified type; if that fails throws an [ClassCastException].
     *
     * @param pluginName The plugin name to get the integration for.
     * @param type       The class of the plugins' integration that extends [PluginIntegration].
     * @param <T>        The specified type of the [PluginIntegration]
     * @return The integration from the plugin of type [T]; null if not available.
     * @throws ClassCastException if the found [PluginIntegration] cannot be cast to [T]
    </T> */
    override fun <T : PluginIntegration> getIntegration(pluginName: String, type: Class<T>): T? {
        val integration = getIntegration(pluginName)
        try {
            return type.cast(integration)
        } catch (ex: ClassCastException) {
            throw ClassCastException("Cannot cast Integration! The integration of plugin \"" + pluginName + "\" is not of type " + type.name)
        }
    }

    /**
     * Runs the specified callback if there is an active PluginIntegration available for that plugin.
     *
     * @param pluginName The plugin name to check for the integration.
     * @param callback   The callback to run.
     */
    override fun runIfAvailable(pluginName: String, callback: Consumer<PluginIntegration>) {
        val integration = getIntegration(pluginName)
        if (integration != null) {
            callback.accept(integration)
        }
    }

    /**
     * Runs the specified callback if there is an active PluginIntegration available for that plugin and the if the integration is of the type specified.
     *
     * @param pluginName The plugin name to check for the integration.
     * @param type       The class that extends [PluginIntegration].
     * @param callback   The callback to run.
     * @param <T>        The type of [PluginIntegration] to check for and use in the callback.
    </T> */
    override fun <T : PluginIntegration> runIfAvailable(pluginName: String, type: Class<T>, callback: Consumer<T>) {
        val integration = getIntegration(pluginName, type)
        if (integration != null) {
            callback.accept(integration)
        }
    }

    override fun evaluateIfAvailable(pluginName: String, callback: Function<PluginIntegration, Boolean>): Boolean {
        val integration = getIntegration(pluginName)
        if (integration != null) {
            return callback.apply(integration)
        }
        return false
    }

    override fun <T : PluginIntegration> evaluateIfAvailable(
        pluginName: String,
        type: Class<T>,
        callback: Function<T, Boolean>
    ): Boolean {
        val integration = getIntegration(pluginName, type)
        if (integration != null) {
            return callback.apply(integration)
        }
        return false
    }

    override fun registerAdapter(pluginName: String, pluginAdapter: Supplier<PluginAdapter>) {
        Optional.ofNullable(Bukkit.getPluginManager().getPlugin(pluginName)).ifPresent {
            registerAdapter(pluginAdapter.get(), adapters = pluginAdapters.computeIfAbsent(pluginName) { HashMap() })
        }
    }

    private fun <T : PluginAdapter?> registerAdapter(adapter: T, adapters: MutableMap<Key, T>) {
        Preconditions.checkArgument(adapter != null, "The plugin adapter cannot be null!")
        Preconditions.checkArgument(
            !adapters.containsKey(adapter!!.namespacedKey),
            "A plugin adapter with that key was already registered!"
        )
        adapters.putIfAbsent(adapter.namespacedKey, adapter)
    }

    override fun <T : PluginAdapter> getAdapter(pluginName: String, type: Class<T>, key: Key): T? {
        val adapters = pluginAdapters[pluginName]
        if (adapters != null) {
            val adapter = adapters[key]
            if (type.isInstance(adapter)) {
                return type.cast(adapter)
            }
        }
        return null
    }
}
