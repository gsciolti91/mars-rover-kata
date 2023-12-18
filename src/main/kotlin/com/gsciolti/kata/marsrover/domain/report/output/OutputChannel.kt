package com.gsciolti.kata.marsrover.domain.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output.None
import com.gsciolti.kata.marsrover.domain.report.output.Output.Value

interface OutputChannel<T> {

    fun display(value: Value<T>)

    fun display(output: Output<T>) =
        when (output) {
            is Value -> display(output)
            is None -> {}
        }
}