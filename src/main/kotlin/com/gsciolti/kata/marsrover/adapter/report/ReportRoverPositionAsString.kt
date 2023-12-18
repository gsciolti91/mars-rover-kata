package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringValue
import com.gsciolti.kata.marsrover.domain.Direction
import com.gsciolti.kata.marsrover.domain.Direction.East
import com.gsciolti.kata.marsrover.domain.Direction.North
import com.gsciolti.kata.marsrover.domain.Direction.South
import com.gsciolti.kata.marsrover.domain.Direction.West
import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.domain.report.Report
import com.gsciolti.kata.marsrover.domain.report.output.Output

object ReportRoverPositionAsString : Report<Rover, String> {

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