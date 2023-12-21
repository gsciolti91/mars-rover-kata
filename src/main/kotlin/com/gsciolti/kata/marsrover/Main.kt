package com.gsciolti.kata.marsrover

import com.gsciolti.kata.marsrover.adapter.command.parse.ParseStringCommand
import com.gsciolti.kata.marsrover.adapter.report.ReportCommandExecutedAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportErrorAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportRoverPositionAsString
import com.gsciolti.kata.marsrover.adapter.report.output.File
import com.gsciolti.kata.marsrover.adapter.report.output.StdOut
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteCommandApi
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.map.Map.Configuration
import com.gsciolti.kata.marsrover.domain.map.plugin.MapPlugin
import com.gsciolti.kata.marsrover.domain.map.plugin.Obstacles
import com.gsciolti.kata.marsrover.domain.map.plugin.boundaries.Boundaries
import com.gsciolti.kata.marsrover.domain.map.plugin.wrap.Wrap
import com.gsciolti.kata.marsrover.domain.model.Coordinates
import com.gsciolti.kata.marsrover.domain.model.Direction.East
import com.gsciolti.kata.marsrover.domain.model.Direction.North
import com.gsciolti.kata.marsrover.domain.model.Direction.South
import com.gsciolti.kata.marsrover.domain.model.Direction.West
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.reportingWith
import com.gsciolti.kata.marsrover.functional.update

fun main(vararg args: String) {

    val params = args.associate {
        val keyValue = it.split("=")
        keyValue[0] to keyValue[1]
    }

    // todo use regex to parse commands

    val startRegex = """^(\d+),(\d+),([nesw])$""".toRegex()
    val (_, x, y, d) = params["-start"]?.let(startRegex::find)!!.groupValues
    val initialRover = Rover(Coordinates(x.toInt(), y.toInt()), d.toDirection())

    val outputFile = params["-out"]?.let(::File)

    val commands = params["-command"]!!.split(",")
    val obstacles = params["-obstacles"]?.let {
        it.split(";")
            .map {
                val rawCoordinates = it.split(",")
                Coordinates(rawCoordinates[0].replace("[", "").toInt(), rawCoordinates[1].replace("]", "").toInt())
            }
    }
        ?.let(::Obstacles)
        ?: Obstacles(emptyList())

    val mapPlugin: MapPlugin? = params["-map"]?.let {
        val rawMapAndType = it.split(",")
        val mapSize = rawMapAndType[0].split("x")
        val mapType = rawMapAndType.getOrElse(1) { "" }

        when (mapType) {
            "" -> ::Boundaries
            "w" -> ::Wrap
            else -> TODO("Unrecognized map type")
        }(mapSize[0].toInt(), mapSize[1].toInt())
    }

    val map = Map {
        apply(mapPlugin)
        apply(obstacles)
    }

    val outputChannel = outputFile?.let { StdOut() + outputFile } ?: StdOut()

    val executeCommand =
        ExecuteCommandApi(ParseStringCommand, map)
            .reportingWith(
                ReportRoverPositionAsString,
                ReportCommandExecutedAsString,
                ReportErrorAsString,
                outputChannel
            )

    commands.update(
        initialState = initialRover,
        updateState = { rover, command ->
            executeCommand(rover, command)
                .map { (_, updatedRover) -> updatedRover }
        })

    outputChannel.flush()
}

private fun String.toDirection() =
    when (this) {
        "n" -> North
        "e" -> East
        "s" -> South
        "w" -> West
        else -> TODO("Direction not recognized")
    }

fun Configuration.apply(plugin: MapPlugin?): Configuration =
    also { plugin?.apply()?.invoke(this) }
