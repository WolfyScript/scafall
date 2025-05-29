package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrap
import com.wolfyscript.scafall.wrappers.world.items.data.CanBreak
import io.papermc.paper.datacomponent.DataComponentTypes

internal val canBreakConverter = PaperDataAPIConverter<CanBreak>(
    {
        val canBreak = unwrap().getData(DataComponentTypes.CAN_BREAK)
        if (canBreak == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        return@PaperDataAPIConverter Result.failure(NotImplementedError())
    }, {
        return@PaperDataAPIConverter Result.failure(NotImplementedError())
    }, {
        unwrap().unsetData(DataComponentTypes.CAN_BREAK)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)