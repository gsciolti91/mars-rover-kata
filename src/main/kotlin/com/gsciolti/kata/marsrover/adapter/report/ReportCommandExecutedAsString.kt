package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringOutput
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.MoveBackward
import com.gsciolti.kata.marsrover.domain.command.MoveForward
import com.gsciolti.kata.marsrover.domain.command.TurnLeft
import com.gsciolti.kata.marsrover.domain.command.TurnRight
import com.gsciolti.kata.marsrover.domain.report.ReportCommandExecuted

object ReportCommandExecutedAsString : ReportCommandExecuted<String> {

    override fun invoke(command: Command): StringOutput =
        when (command) {
            is MoveForward -> StringOutput("Rover moved forward")
            is MoveBackward -> StringOutput("Rover moved backward")
            is TurnLeft -> StringOutput("Rover turned left")
            is TurnRight -> StringOutput("Rover turned right")
        }
}