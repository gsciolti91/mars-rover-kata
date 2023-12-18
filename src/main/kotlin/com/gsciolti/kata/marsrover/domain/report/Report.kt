package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.report.output.Output

interface Report<IN, OUT> : (IN) -> Output<OUT>
