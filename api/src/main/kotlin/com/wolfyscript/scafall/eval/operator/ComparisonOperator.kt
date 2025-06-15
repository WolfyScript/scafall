package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JacksonInject
import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider

/**
 * Represents comparison operators that compare values.<br></br>
 *
 *  * equal (==) [ComparisonOperatorEqual]
 *  * not equal (!=) [ComparisonOperatorNotEqual]
 *  * less (<) [ComparisonOperatorLess]
 *  * less or equal(<=) [ComparisonOperatorLessEqual]
 *  * greater (>) [ComparisonOperatorGreater]
 *  * greater or equal (>=) [ComparisonOperatorGreaterEqual]
 *
 *
 * @param <V> The type of the objects to compare. Must be the same for both objects.
</V> */
abstract class ComparisonOperator<V : Comparable<V>?> protected constructor(
    @JacksonInject wolfyUtils: PluginWrapper,
    @JvmField protected var thisValue: ValueProvider<V>,
    @JvmField protected var thatValue: ValueProvider<V>
) :
    BoolOperator() {
    abstract override fun evaluate(context: EvalContext): Boolean
}
