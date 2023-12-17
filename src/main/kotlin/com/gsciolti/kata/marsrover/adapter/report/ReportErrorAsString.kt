package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringOutput
import com.gsciolti.kata.marsrover.domain.command.parse.CommandNotValid
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.report.ReportError

object ReportErrorAsString : ReportError<String> {

    override fun invoke(error: Any): StringOutput =
        when (error) {
            is CommandNotValid -> StringOutput("Invalid command '${error.rawCommand}'")
            is Map.BoundaryEncountered -> StringOutput("Boundary encountered at [${error.move.currentRover.position.x},${error.move.currentRover.position.y}]")
            is Map.ObstacleEncountered -> StringOutput("Obstacle encountered at [${error.move.nextRover.position.x},${error.move.nextRover.position.y}]")
            // todo model error and remove
            else -> TODO("error not handled")
        }
}