package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.MultiLineStringValue
import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.domain.command.AtomicCommand
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.MoveBackward
import com.gsciolti.kata.marsrover.domain.command.MoveForward
import com.gsciolti.kata.marsrover.domain.command.TurnLeft
import com.gsciolti.kata.marsrover.domain.command.TurnRight
import com.gsciolti.kata.marsrover.domain.model.Direction
import com.gsciolti.kata.marsrover.domain.model.Direction.East
import com.gsciolti.kata.marsrover.domain.model.Direction.North
import com.gsciolti.kata.marsrover.domain.model.Direction.South
import com.gsciolti.kata.marsrover.domain.model.Direction.West
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.ReportCommandExecuted
import com.gsciolti.kata.marsrover.domain.report.output.Output

object ReportCommandExecutedAsString : ReportCommandExecuted<String> {

    // todo extract report rover position
    // todo move logic to command?
    override fun invoke(command: Command, rover: Rover): Output<String> =
        when (command) {
            is MoveForward -> StringValue("Rover moved forward") + StringValue("Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            is MoveBackward -> StringValue("Rover moved backward") + StringValue("Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            is TurnLeft -> StringValue("Rover turned left") + StringValue("Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            is TurnRight -> StringValue("Rover turned right") + StringValue("Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            is AtomicCommand -> MultiLineStringValue(
                command.commandsExecuted().map { (command, rover) -> invoke(command, rover) })
        }
}

private fun Direction.asString() =
    when (this) {
        North -> "n"
        East -> "e"
        South -> "s"
        West -> "w"
    }