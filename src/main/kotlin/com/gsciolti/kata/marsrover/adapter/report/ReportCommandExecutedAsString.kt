package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.MoveBackward
import com.gsciolti.kata.marsrover.domain.command.MoveForward
import com.gsciolti.kata.marsrover.domain.command.TurnLeft
import com.gsciolti.kata.marsrover.domain.command.TurnRight
import com.gsciolti.kata.marsrover.domain.report.Report
import com.gsciolti.kata.marsrover.domain.report.output.Output

object ReportCommandExecutedAsString : Report<Command, String> {

    override fun invoke(command: Command): Output<String> =
        when (command) {
            is MoveForward -> StringValue("Rover moved forward")
            is MoveBackward -> StringValue("Rover moved backward")
            is TurnLeft -> StringValue("Rover turned left")
            is TurnRight -> StringValue("Rover turned right")
        }
}