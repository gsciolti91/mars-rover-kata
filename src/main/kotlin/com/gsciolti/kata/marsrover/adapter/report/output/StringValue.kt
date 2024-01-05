package com.gsciolti.kata.marsrover.adapter.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output
import com.gsciolti.kata.marsrover.domain.report.output.Output.Value

class StringValue(value: String) : Value<String>(value) {

    override operator fun plus(other: Value<String>): Value<String> =
        StringValue("$value. ${other.value}")
}

fun List<Output<String>>.join(separator: String) =
    StringValue(filterIsInstance<Value<String>>().joinToString(separator) { it.value })
