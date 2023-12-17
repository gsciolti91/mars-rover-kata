package com.gsciolti.kata.marsrover.adapter.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output

class StringOutput(value: String) : Output<String>(value) {

    override operator fun plus(other: Output<String>): Output<String> =
        StringOutput("$value. ${other.value}")
}