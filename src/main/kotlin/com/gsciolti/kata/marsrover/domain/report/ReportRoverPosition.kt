package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.domain.report.output.Output

interface ReportRoverPosition<T> : (Rover) -> Output<T>