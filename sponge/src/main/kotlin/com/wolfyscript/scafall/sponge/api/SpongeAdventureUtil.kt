package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.AdventureUtil
import com.wolfyscript.scafall.Scafall
import net.kyori.adventure.audience.Audience
import org.spongepowered.api.Sponge
import org.spongepowered.api.adventure.Audiences
import java.util.*

class SpongeAdventureUtil(private val scafall: Scafall) : AdventureUtil {

    override fun player(uuid: UUID): Audience {
        return Sponge.server().player(uuid).orElseThrow()
    }

    override fun all(): Audience {
        return Audiences.server()
    }

    override fun system(): Audience {
        return Audiences.system()
    }

}