package com.gsciolti.kata.marsrover.domain.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output.Value

interface OutputChannel<T> {

    fun flush()

    fun display(value: Value<T>)

    fun display(output: Output<T>) =
        when (output) {
            is Value -> display(output)
            is Output.None -> {}
        }

    operator fun plus(other: OutputChannel<T>): OutputChannel<T> =
        MultipleOutputChannel(this, other)

    class None<T> : OutputChannel<T> {
        override fun flush() {
        }

        override fun display(value: Value<T>) {
        }
    }
}
