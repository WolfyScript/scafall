package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.PluginWrapper

/**
 * An Operator that represents logical operators like and (&&), or (||), not (!).<br></br>
 * They evaluate at least one inner [BoolOperator], which then results in a booleanish output.
 *
 *
 *  * [LogicalOperatorAnd]
 *  * [LogicalOperatorOr]
 *  * [LogicalOperatorNot]
 *
 *
 */
abstract class LogicalOperator(
    @JacksonInject wolfyUtils: PluginWrapper, @JvmField @field:JsonProperty(
        "this"
    ) protected val thisValue: BoolOperator
) :
    BoolOperator()
