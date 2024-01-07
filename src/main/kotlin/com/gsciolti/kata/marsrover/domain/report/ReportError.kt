package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError
import com.gsciolti.kata.marsrover.domain.report.output.Output

interface ReportError<OUT> : (MarsRoverError) -> Output<OUT>
