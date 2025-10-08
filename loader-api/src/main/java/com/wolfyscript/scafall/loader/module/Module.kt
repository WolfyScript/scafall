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
}

interface Client {

    fun onLoad()

    fun onUnload()

}

interface Server {

    fun onLoad()

    fun onUnload()

}
