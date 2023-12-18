package com.gsciolti.kata.marsrover.adapter.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output.Value
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel

object StdOut : OutputChannel<String> {

    override fun display(value: Value<String>) =
        println(value.value)
}