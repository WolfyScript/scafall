package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.wrappers.world.items.data.Profile
import org.bukkit.Bukkit
import org.bukkit.inventory.meta.SkullMeta

internal val profileItemMetaConverter = if (ScafallProvider.get().platformType == PlatformType.PAPER) {
    ItemMetaDataKeyConverter<Profile>({
        if (this !is SkullMeta) {
            return@ItemMetaDataKeyConverter null
        }
        playerProfile?.let {
            Profile(it.id, it.name, Profile.Textures(it.textures.skin, it.textures.cape))
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
            Profile(it.uniqueId, it.name, Profile.Textures(it.textures.skin, it.textures.cape))
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
