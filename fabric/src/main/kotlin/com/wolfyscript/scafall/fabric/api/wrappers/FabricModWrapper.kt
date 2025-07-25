package com.wolfyscript.scafall.fabric.api.wrappers

import com.wolfyscript.scafall.ModWrapper
import net.fabricmc.loader.api.ModContainer
import org.slf4j.Logger

class FabricModWrapper(mod: ModContainer, override val logger: Logger) : ModWrapper {

    override val name: String = mod.metadata.name

}