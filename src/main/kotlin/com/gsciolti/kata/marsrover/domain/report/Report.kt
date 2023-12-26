package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.output.Output

interface Report<IN, OUT> : (IN) -> Output<OUT>

interface ReportCommandExecuted<OUT> : (Command, Rover) -> Output<OUT>
