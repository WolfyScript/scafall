package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.config.jackson.JacksonUtil
import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.wrappers.world.items.data.Profile
import org.spongepowered.api.data.Keys
import org.spongepowered.api.profile.property.ProfileProperty
import java.net.URL
import kotlin.jvm.optionals.getOrNull

internal val profileDataConverter = SpongeItemStackDataComponentConverter<Profile>({
    get(Keys.GAME_PROFILE).map { profile ->
        // TODO: wrap texture properties
        val textureProperty = profile.properties().find {
            it.name() == "textures"
        }?.value() // Should be fine for now, because there isn't really another property
        val parsedTextureProperties =
            JacksonUtil.objectMapper.readValue(textureProperty, ProfileTexturesProperties::class.java)
        Profile(
            profile.uuid(),
            profile.name().getOrNull(),
            Profile.Textures(
                parsedTextureProperties.skin?.url?.let { URL(it) },
                parsedTextureProperties.cape?.url?.let { URL(it) }
            )
        )
    }.getOrNull()
}, {
    if (it.id != null) {
        val gameProfile = org.spongepowered.api.profile.GameProfile.of(it.id, it.name)
            .withProperty(ProfileProperty.of("textures", "")) // TODO

        offer(Keys.GAME_PROFILE, gameProfile)
    }
})

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProfileTexturesProperties(
    @JsonProperty("SKIN") val skin: SkinProperty?,
    @JsonProperty("CAPE") val cape: CapeProperty?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SkinProperty(val url: String, val metadata: String?) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Metadata(val model: String)

}

@JsonIgnoreProperties(ignoreUnknown = true)
data class CapeProperty(val url: String)
