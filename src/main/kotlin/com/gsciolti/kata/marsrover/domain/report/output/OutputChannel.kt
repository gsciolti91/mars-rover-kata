package com.gsciolti.kata.marsrover.domain.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output.None
import com.gsciolti.kata.marsrover.domain.report.output.Output.Value

interface OutputChannel<T> {

    fun flush()

    fun display(value: Value<T>)

    fun display(output: Output<T>) =
        when (output) {
            is Value -> display(output)
            is None -> {}
        }

    operator fun plus(other: OutputChannel<T>): OutputChannel<T> =
        MultipleOutputChannel(this, other)
}

