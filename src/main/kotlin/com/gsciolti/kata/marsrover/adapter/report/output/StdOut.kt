package com.gsciolti.kata.marsrover.adapter.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel

// todo try to make general
object StdOut : OutputChannel<String> {

    override fun display(output: Output<String>) =
        println(output.value)
}