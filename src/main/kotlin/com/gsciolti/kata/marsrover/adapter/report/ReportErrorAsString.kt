package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.domain.command.execute.error.BoundaryEncountered
import com.gsciolti.kata.marsrover.domain.command.execute.error.CommandNotValid
import com.gsciolti.kata.marsrover.domain.command.execute.error.ErrorPrevented
import com.gsciolti.kata.marsrover.domain.command.execute.error.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.command.execute.error.ObstacleEncountered
import com.gsciolti.kata.marsrover.domain.report.Report
import com.gsciolti.kata.marsrover.domain.report.output.Output

object ReportErrorAsString : Report<ExecuteCommandError, String> {

    override fun invoke(error: ExecuteCommandError): Output<String> =
        when (error) {
            is CommandNotValid -> StringValue("Invalid command '${error.rawCommand}'")
            is BoundaryEncountered -> StringValue("Boundary encountered at [${error.move.currentRover.position.x},${error.move.currentRover.position.y}]")
            is ObstacleEncountered -> StringValue("Obstacle encountered at [${error.move.nextRover.position.x},${error.move.nextRover.position.y}]")
            is ErrorPrevented ->
                when (error.error) {
                    is BoundaryEncountered -> StringValue("Boundary would be hit at [${error.error.move.currentRover.position.x},${error.error.move.currentRover.position.y}]")
                    is CommandNotValid -> TODO("Prevented command not valid")
                    is ObstacleEncountered -> TODO("Prevented obstacle encountered")
                    is ErrorPrevented -> invoke(error)
                }
        }
}