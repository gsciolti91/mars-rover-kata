package com.gsciolti.kata.marsrover

import com.gsciolti.kata.marsrover.adapter.command.parse.ParseStringCommand
import com.gsciolti.kata.marsrover.adapter.report.ReportCommandExecutedAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportErrorAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportRoverPositionAsString
import com.gsciolti.kata.marsrover.adapter.report.output.StdOut
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteCommandApi
import com.gsciolti.kata.marsrover.domain.map.BoundedMap
import com.gsciolti.kata.marsrover.domain.map.OpenMap
import com.gsciolti.kata.marsrover.domain.map.WrappingMap
import com.gsciolti.kata.marsrover.domain.model.Coordinates
import com.gsciolti.kata.marsrover.domain.model.Direction.East
import com.gsciolti.kata.marsrover.domain.model.Direction.North
import com.gsciolti.kata.marsrover.domain.model.Direction.South
import com.gsciolti.kata.marsrover.domain.model.Direction.West
import com.gsciolti.kata.marsrover.domain.model.Obstacles
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.reportingWith
import com.gsciolti.kata.marsrover.functional.update

fun main(vararg args: String) {

    val params = args.associate {
        val keyValue = it.split("=")
        keyValue[0] to keyValue[1]
    }

    val startParams = params["-start"]!!.split(",")
    val commands = params["-command"]!!.split(",")
    val obstacles = params["-obstacles"]?.let {
        it.split(";")
            .map {
                val rawCoordinates = it.split(",")
                Coordinates(rawCoordinates[0].replace("[", "").toInt(), rawCoordinates[1].replace("]", "").toInt())
            }
    }
        ?.let(::Obstacles) ?: Obstacles(emptyList())

    val map = params["-map"]?.let {
        val rawMapAndType = it.split(",")
        val mapDimensions = rawMapAndType[0].split("x")
        val mapType = rawMapAndType.getOrElse(1) { "" }

        when (mapType) {
            "" -> BoundedMap(mapDimensions[0].toInt(), mapDimensions[1].toInt(), obstacles)
            "w" -> WrappingMap(mapDimensions[0].toInt(), mapDimensions[1].toInt(), obstacles)
            else -> TODO("Unrecognized map type")
        }
    } ?: OpenMap(obstacles)

    val executeCommand =
        ExecuteCommandApi(ParseStringCommand, map)
            .reportingWith(
                ReportRoverPositionAsString,
                ReportCommandExecutedAsString,
                ReportErrorAsString,
                StdOut
            )

    val initialRover = Rover(
        Coordinates(startParams[0].toInt(), startParams[1].toInt()),
        startParams[2].toDirection()
    )

    commands.update(
        initialState = initialRover,
        updateState = { rover, command ->
            executeCommand(rover, command)
                .map { (_, updatedRover) -> updatedRover }
        })
}

private fun String.toDirection() =
    when (this) {
        "n" -> North
        "e" -> East
        "s" -> South
        "w" -> West
        else -> TODO("Direction not recognized")
    }
