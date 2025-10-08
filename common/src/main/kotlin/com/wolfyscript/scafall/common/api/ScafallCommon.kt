package com.wolfyscript.scafall.common.api

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.common.api.compat.DependencyManagerCommon
import com.wolfyscript.scafall.common.api.jackson.JacksonUtilImpl
import com.wolfyscript.scafall.compat.DependencyManager
import com.wolfyscript.scafall.config.jackson.JacksonUtil

abstract class ScafallCommon() : Scafall {

    override val jacksonUtil: JacksonUtil = JacksonUtilImpl()
    override val dependencyManager: DependencyManager = DependencyManagerCommon()
    override val client = null

}