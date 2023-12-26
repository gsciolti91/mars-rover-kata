package com.gsciolti.kata.marsrover.adapter.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output
import com.gsciolti.kata.marsrover.domain.report.output.Output.Value

class StringValue(value: String) : Value<String>(value) {

    override operator fun plus(other: Value<String>): Value<String> =
        StringValue("$value. ${other.value}")
}

// todo extract / convert to ext fun
class MultiLineStringValue(values: List<Output<String>>) : Value<String>(
    values.filterIsInstance<Value<String>>().joinToString("\n") { it.value }) {

    override operator fun plus(other: Value<String>): Value<String> =
        TODO("Not needed so far")
}