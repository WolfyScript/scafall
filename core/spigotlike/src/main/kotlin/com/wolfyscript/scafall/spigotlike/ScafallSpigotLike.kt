package com.wolfyscript.scafall.spigotlike

import com.wolfyscript.scafall.ScafallCommon
import com.wolfyscript.scafall.factories.CommonFactories
import com.wolfyscript.scafall.registry.ScafallCommonRegistries
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ScafallRegistryTypes
import com.wolfyscript.scafall.spigotlike.compat.denizen.DenizenDependency
import com.wolfyscript.scafall.spigotlike.compat.eco.EcoDependency
import com.wolfyscript.scafall.spigotlike.compat.executableblocks.ExecutableBlocksDependency
import com.wolfyscript.scafall.spigotlike.compat.executableitems.ExecutableItemsDependency
import com.wolfyscript.scafall.spigotlike.compat.itemsadder.ItemsAdderDependency
import com.wolfyscript.scafall.spigotlike.compat.magic.MagicDependency
import com.wolfyscript.scafall.spigotlike.compat.mmoitems.MMOItemsDependency
import com.wolfyscript.scafall.spigotlike.compat.mythicmobs.MythicMobsDependency
import com.wolfyscript.scafall.spigotlike.compat.oraxen.OraxenDependency

abstract class ScafallSpigotLike : ScafallCommon() {

    abstract override val registries: ScafallCommonRegistries
    abstract override val factories: CommonFactories

    override fun onInit() {
        super.onInit()

        factories.init()
        registries.initRegistries()
        registries.registerForJackson()

        ScafallRegistryTypes.dependencies.resolveOrThrow().apply {
            register(Key.scafall("plugins/${DenizenDependency.ID}"), DenizenDependency::class.java)
            register(Key.scafall("plugins/${EcoDependency.ID}"), EcoDependency::class.java)
            register(Key.scafall("plugins/${ExecutableItemsDependency.ID}"), ExecutableItemsDependency::class.java)
            register(Key.scafall("plugins/${ExecutableBlocksDependency.ID}"), ExecutableBlocksDependency::class.java)
            register(Key.scafall("plugins/${ItemsAdderDependency.ID}"), ItemsAdderDependency::class.java)
            register(Key.scafall("plugins/${MagicDependency.ID}"), MagicDependency::class.java)
            register(Key.scafall("plugins/${MMOItemsDependency.ID}"), MMOItemsDependency::class.java)
            register(Key.scafall("plugins/${MythicMobsDependency.ID}"), MythicMobsDependency::class.java)
            register(Key.scafall("plugins/${OraxenDependency.ID}"), OraxenDependency::class.java)
        }
    }

}