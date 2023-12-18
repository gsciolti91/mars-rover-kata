package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.domain.command.parse.CommandNotValid
import com.gsciolti.kata.marsrover.domain.map.Map.BoundaryEncountered
import com.gsciolti.kata.marsrover.domain.map.Map.ObstacleEncountered
import com.gsciolti.kata.marsrover.domain.report.Report
import com.gsciolti.kata.marsrover.domain.report.output.Output

object ReportErrorAsString : Report<Any, String> {

    override fun invoke(error: Any): Output<String> =
        when (error) {
            is CommandNotValid -> StringValue("Invalid command '${error.rawCommand}'")
            is BoundaryEncountered -> StringValue("Boundary encountered at [${error.move.currentRover.position.x},${error.move.currentRover.position.y}]")
            is ObstacleEncountered -> StringValue("Obstacle encountered at [${error.move.nextRover.position.x},${error.move.nextRover.position.y}]")
            // todo model error and remove
            else -> TODO("error not handled")
        }
}