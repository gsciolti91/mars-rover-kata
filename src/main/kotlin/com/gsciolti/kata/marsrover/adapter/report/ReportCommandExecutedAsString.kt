package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.adapter.report.output.join
import com.gsciolti.kata.marsrover.domain.command.AtomicCommand
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.MoveBackward
import com.gsciolti.kata.marsrover.domain.command.MoveForward
import com.gsciolti.kata.marsrover.domain.command.TurnLeft
import com.gsciolti.kata.marsrover.domain.command.TurnRight
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.ReportCommandExecuted
import com.gsciolti.kata.marsrover.domain.report.ReportRoverPosition
import com.gsciolti.kata.marsrover.domain.report.output.Output
import com.gsciolti.kata.marsrover.domain.report.output.plus

class ReportCommandExecutedAsString(
    private val reportRoverPosition: ReportRoverPosition<String>
) : ReportCommandExecuted<String> {

    override fun invoke(command: Command, rover: Rover): Output<String> =
        when (command) {
            is MoveForward -> StringValue("Rover moved forward") + reportRoverPosition(rover)
            is MoveBackward -> StringValue("Rover moved backward") + reportRoverPosition(rover)
            is TurnLeft -> StringValue("Rover turned left") + reportRoverPosition(rover)
            is TurnRight -> StringValue("Rover turned right") + reportRoverPosition(rover)
            is AtomicCommand -> command.commandsExecuted().map { (command, rover) -> invoke(command, rover) }.join("\n")
        }
}
