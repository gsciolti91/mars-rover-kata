package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.report.output.Output

interface ReportCommandExecuted<T> : (Command) -> Output<T>