package com.wolfyscript.scafall.common.api

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.jackson.JacksonUtilImpl
import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.loader.module.BasicModule
import com.wolfyscript.scafall.loader.module.Client
import com.wolfyscript.scafall.server.ScafallServer

abstract class ScafallCommon : BasicModule<ScafallServer, Client>(), Scafall {

    override val jacksonUtil: JacksonUtil = JacksonUtilImpl()
    override val dependencyManager: DependencyManager = DependencyManager.createNew()

}