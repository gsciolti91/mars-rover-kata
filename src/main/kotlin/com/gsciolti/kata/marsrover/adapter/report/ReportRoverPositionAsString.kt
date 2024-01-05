package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.domain.model.Direction
import com.gsciolti.kata.marsrover.domain.model.Direction.East
import com.gsciolti.kata.marsrover.domain.model.Direction.North
import com.gsciolti.kata.marsrover.domain.model.Direction.South
import com.gsciolti.kata.marsrover.domain.model.Direction.West
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.ReportRoverPosition
import com.gsciolti.kata.marsrover.domain.report.output.Output

object ReportRoverPositionAsString : ReportRoverPosition<String> {

    override fun invoke(rover: Rover): Output<String> =
        StringValue("Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
}

private fun Direction.asString() =
    when (this) {
        North -> "n"
        East -> "e"
        South -> "s"
        West -> "w"
    }