package com.wolfyscript.scafall.loader.module

/**
 * Simplifies the instantiation and load behaviour of public API across platforms.
 *
 * The module itself may provide API features that are available across all client and server environments.
 *
 * The [client] and [server] APIs each provide features only available in their specific environment.
 * The types of which are defined via the generics:
 * * [S] The type of the server API interface. Available on Client and Dedicated Server
 * * [C] The type of the client API interface. Available on Client
 */
interface Module<S: Server, C: Client> {

    /**
     * Called after the module has been instantiated and registered.
     */
    fun onInit() {}

    /**
     * API Features only available when loaded on a server.
     * i.a. Client (local server), or Dedicated Server
     */
    val server: S?

    /**
     * API Features only available when loaded on a client.
     */
    val client: C?

    fun onServerAvailable(fn: (server: S) -> Unit)

    fun onClientAvailable(fn: (client: C) -> Unit)

}

abstract class BasicModule<S: Server, C: Client> : Module<S, C> {

    override fun onInit() {}

    override var server: S? = null
        protected set(value) {
            field = value
            if (value != null) {
                serverListeners.forEach { it(value) }
                serverListeners.clear()
            }
        }

    /**
     * API Features only available when loaded on a client.
     */
    override var client: C? = null
        protected set(value) {
            field = value
            if (value != null) {
                clientListeners.forEach { it(value) }
                clientListeners.clear()
            }
        }

    private val serverListeners: MutableList<(S) -> Unit> = mutableListOf()
    private val clientListeners: MutableList<(C) -> Unit> = mutableListOf()

    override fun onServerAvailable(fn: (server: S) -> Unit) {
        if (server == null) {
            serverListeners.add(fn)
        } else {
            fn(server!!)
        }
    }

    override fun onClientAvailable(fn: (client: C) -> Unit) {
        if (client == null) {
            clientListeners.add(fn)
        } else {
            fn(client!!)
        }
    }

}

interface Client {

    fun onLoad()

    fun onUnload()

}

interface Server {

    fun onLoad()

    fun onUnload()

}
