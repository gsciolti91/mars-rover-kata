package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.output.Output

interface ReportRoverPosition<OUT> : (Rover) -> Output<OUT>
