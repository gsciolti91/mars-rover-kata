package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.output.Output

interface ReportError<OUT> : (MarsRoverError, Rover) -> Output<OUT>
