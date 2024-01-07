package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.domain.command.execute.error.BoundaryEncountered
import com.gsciolti.kata.marsrover.domain.command.execute.error.ExecuteCommandErrorPrevented
import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError
import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError.CommandNotValid
import com.gsciolti.kata.marsrover.domain.command.execute.error.ObstacleEncountered
import com.gsciolti.kata.marsrover.domain.report.ReportError
import com.gsciolti.kata.marsrover.domain.report.ReportRoverPosition
import com.gsciolti.kata.marsrover.domain.report.output.Output
import com.gsciolti.kata.marsrover.domain.report.output.plus

class ReportErrorAsString(
    private val reportRoverPosition: ReportRoverPosition<String>
) : ReportError<String> {

    override fun invoke(error: MarsRoverError): Output<String> =
        reportError(error) + reportRoverPosition(error.lastValidRover)

    private fun reportError(error: MarsRoverError): Output<String> =
        when (error) {
            is CommandNotValid<*> -> StringValue("Invalid command '${error.rawCommand}'")
            is BoundaryEncountered -> StringValue("Boundary encountered at [${error.move.currentRover.position.x},${error.move.currentRover.position.y}]")
            is ObstacleEncountered -> StringValue("Obstacle encountered at [${error.move.nextRover.position.x},${error.move.nextRover.position.y}]")
            is ExecuteCommandErrorPrevented ->
                when (error.error) {
                    is BoundaryEncountered -> StringValue("Error prevented: Boundary encountered at [${error.error.move.currentRover.position.x},${error.error.move.currentRover.position.y}]")
                    is ObstacleEncountered -> StringValue("Error prevented: Obstacle encountered at [${error.error.move.nextRover.position.x},${error.error.move.nextRover.position.y}]")
                    is ExecuteCommandErrorPrevented -> TODO("Never the case so far")
                }
        }
}
