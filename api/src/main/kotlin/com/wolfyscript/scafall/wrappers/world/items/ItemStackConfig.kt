package com.wolfyscript.scafall.wrappers.world.items

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.wolfyscript.scafall.config.jackson.RegistryKeyTypeIdResolver
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.Key
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * A cross-platform ItemStack configuration using the jackson library.
 *
 * The [stack] is stored as vanilla SNBT in the config and used as the base on which changes are applied.
 * [Overrides][Override] can be used to manipulate the [stack] upon creating it.
 *
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
interface ItemStackConfig {

    /**
     * The id of the item in the `<namespace>:<item_key>` format.
     */
    @get:JsonProperty("stack")
    val stack: ItemStackSnapshot

    /**
     * The amount of the created stack. The value may be computed from other sources.
     */
    var amount: ValueProvider<Int>

    /**
     * The overrides that are loaded and will be applied to the stack.
     */
    @get:JsonIgnore
    val overrides: Map<Key, Override>

    /**
     * Constructs the implementation specific ItemStack from the settings.
     * The context allows settings to use contextual data to create the ItemStack data.
     *
     * @param context The context to use.
     * @return The constructed ItemStack.
     */
    fun constructItemStack(
        context: EvalContext = EvalContext(),
        miniMessage: MiniMessage? = MiniMessage.miniMessage(),
        tagResolvers: TagResolver = TagResolver.empty(),
    ): ItemStack?

    /**
     * An override specifies settings that are applied to the [ItemStack] created from an [ItemStackConfig].
     * Therefore, an override is designed to be serializable and configurable.
     *
     * The values applied by an override can also adapt to the given [EvalContext].
     *
     * They should not be confused with the vanilla Data Components. Overrides use Data Components internally to apply data to the
     * ItemStack, but they are not necessarily 1:1 wrappers, as they can be used for more complex custom behavior.
     */
    @JsonTypeIdResolver(RegistryKeyTypeIdResolver::class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonPropertyOrder(value = ["type"])
    interface Override {

        /**
         * Applies this override to the [ItemStack] that is being constructed.
         */
        fun applyTo(itemStack: ItemStack, context: EvalContext, miniMessage: MiniMessage?, tagResolvers: TagResolver)

    }

}
