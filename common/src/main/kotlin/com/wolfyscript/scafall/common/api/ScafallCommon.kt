package com.wolfyscript.scafall.common.api

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.compat.DependencyManagerCommon
import com.wolfyscript.scafall.common.api.jackson.JacksonUtilImpl
import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.loader.module.Client
import com.wolfyscript.scafall.server.ScafallServer

abstract class ScafallCommon() : Scafall {

    override val jacksonUtil: JacksonUtil = JacksonUtilImpl()
    override val dependencyManager: DependencyManager = DependencyManagerCommon()
    override var client = null
        set(value) {
            field = value
            if (value != null) {
                clientListeners.forEach { it(value) }
                clientListeners.clear()
            }
        }

    private val serverListeners: MutableList<(ScafallServer) -> Unit> = mutableListOf()
    private val clientListeners: MutableList<(Client) -> Unit> = mutableListOf()

    override var server: ScafallServer? = null
        set(value) {
            field = value
            if (value != null) {
                serverListeners.forEach { it(value) }
                serverListeners.clear()
            }
        }


    override fun onServerAvailable(fn: (server: ScafallServer) -> Unit) {
        if (server == null) {
            serverListeners.add(fn)
        } else {
            fn(server!!)
        }
    }

    override fun onClientAvailable(fn: (client: Client) -> Unit) {
        if (client == null) {
            clientListeners.add(fn)
        } else {
            fn(client!!)
        }
    }

}