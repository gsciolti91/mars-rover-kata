package com.gsciolti.kata.marsrover.adapter.report

import com.gsciolti.kata.marsrover.adapter.report.output.StringOutput
import com.gsciolti.kata.marsrover.domain.Direction
import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.domain.report.ReportRoverPosition

object ReportRoverPositionAsString : ReportRoverPosition<String> {
    override fun invoke(rover: Rover): StringOutput =
        StringOutput("Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
}

private fun Direction.asString() =
    when (this) {
        Direction.North -> "n"
        Direction.East -> "e"
        Direction.South -> "s"
        Direction.West -> "w"
    }