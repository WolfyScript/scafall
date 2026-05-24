package com.wolfyscript.scafall.wrappers.world.entity

import net.minecraft.world.entity.player.Player

@JvmInline
internal value class ScafallPlayerImpl(val player: Player) : ScafallPlayer
