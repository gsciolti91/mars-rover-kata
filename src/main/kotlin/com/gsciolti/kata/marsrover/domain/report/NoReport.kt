package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.report.output.Output

class NoReport<IN, OUT> : Report<IN, OUT> {

    override fun invoke(s: IN): Output<OUT> = Output.None
}