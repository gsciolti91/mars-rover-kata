package com.gsciolti.kata.marsrover.adapter.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output.Value
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel

class File(path: String) : OutputChannel<String> {

    private val file = java.io.File(path)
    private val buffer = StringBuilder()

    override fun display(value: Value<String>) {
        buffer.append("${value.value}\n")
    }

    override fun flush() {
        file.writeText(buffer.toString())
        buffer.clear()
    }
}