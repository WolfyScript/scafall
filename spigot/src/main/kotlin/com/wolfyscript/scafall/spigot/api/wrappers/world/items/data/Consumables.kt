package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.wrappers.world.items.data.Consumable
import com.wolfyscript.scafall.wrappers.world.items.data.Food

internal val consumableItemMetaConverter = ItemMetaDataKeyConverter<Consumable>({
    null
},{

})

internal val foodItemMetaConverter = ItemMetaDataKeyConverter<Food>(
    {
        if (hasFood()) {
            val food = food
            return@ItemMetaDataKeyConverter Food(food.nutrition, food.saturation, food.canAlwaysEat())
        }
        null
    }, {
        val food = this.food
        food.nutrition = it.nutrition
        food.saturation = it.saturation
        food.setCanAlwaysEat(it.canAlwaysEat)
        setFood(food)
    }
)