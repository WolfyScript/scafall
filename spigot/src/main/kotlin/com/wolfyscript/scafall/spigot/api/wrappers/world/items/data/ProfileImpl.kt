package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.ProfileImpl.TexturesImpl
import com.wolfyscript.scafall.wrappers.world.items.data.Profile
import org.bukkit.Bukkit
import org.bukkit.inventory.meta.SkullMeta
import java.net.URL
import java.util.*

internal val profileItemMetaConverter = if (ScafallProvider.get().platformType == PlatformType.PAPER) {
    ItemMetaDataKeyConverter<Profile>({
        if (this !is SkullMeta) {
            return@ItemMetaDataKeyConverter null
        }
        playerProfile?.let {
            ProfileImpl(it.id, it.name, TexturesImpl(it.textures.skin, it.textures.cape))
        }
    }, { profile ->
        if (this !is SkullMeta) {
            return@ItemMetaDataKeyConverter
        }
        playerProfile = playerProfile ?: Bukkit.createProfile(profile.id, profile.name)
        playerProfile?.let {
            it.textures.skin = profile.textures.skin
            it.textures.cape = profile.textures.cape
        }
    })
} else {
    ItemMetaDataKeyConverter<Profile>({
        if (this !is SkullMeta) {
            return@ItemMetaDataKeyConverter null
        }
        ownerProfile?.let {
            ProfileImpl(it.uniqueId, it.name, TexturesImpl(it.textures.skin, it.textures.cape))
        }
    }, { profile ->
        if (this !is SkullMeta) {
            return@ItemMetaDataKeyConverter
        }
        ownerProfile = ownerProfile ?: Bukkit.createPlayerProfile(profile.id, profile.name)
        ownerProfile?.let {
            it.textures.skin = profile.textures.skin
            it.textures.cape = profile.textures.cape
        }
    })
}

class ProfileImpl(override var id: UUID?, override var name: String?, override var textures: Profile.Textures) :
    Profile {

    override fun isComplete(): Boolean {
        return id != null && name != null && !textures.isEmpty()
    }

    class TexturesImpl(override var skin: URL?, override var cape: URL?) : Profile.Textures {
        override fun isEmpty(): Boolean {
            return cape == null && skin == null
        }
    }

}