package com.gsciolti.kata.marsrover.adapter.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output.Value
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel

class StdOut : OutputChannel<String> {

    private val buffer = StringBuilder()

    override fun display(value: Value<String>) {
        buffer.append("${value.value}\n")
    }

    override fun flush() {
        print(buffer.toString())
        buffer.clear()
    }
}