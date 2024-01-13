package com.gsciolti.kata.marsrover

import com.gsciolti.kata.marsrover.adapter.command.parse.ParseAtomicStringCommand
import com.gsciolti.kata.marsrover.adapter.command.parse.ParseSimpleStringCommand
import com.gsciolti.kata.marsrover.adapter.report.ReportCommandExecutedAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportErrorAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportRoverPositionAsString
import com.gsciolti.kata.marsrover.adapter.report.output.File
import com.gsciolti.kata.marsrover.adapter.report.output.StdOut
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteSingleCommand
import com.gsciolti.kata.marsrover.domain.command.execute.handlingMultipleCommands
import com.gsciolti.kata.marsrover.domain.command.parse.CascadingParseCommand
import com.gsciolti.kata.marsrover.domain.map.Map
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
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel
import com.gsciolti.kata.marsrover.domain.report.reportingWith

fun main(vararg args: String) {

    // todo extract parse to functions
    val params = args.associate {
        val keyValue = it.split("=")
        keyValue[0] to keyValue[1]
    }

    val startRegex = """^(\d+),(\d+),([nesw])$""".toRegex()
    val (_, x, y, d) = params["-start"]?.let(startRegex::find)!!.groupValues
    val initialRover = Rover(Coordinates(x.toInt(), y.toInt()), d.toDirection())

    val outputFile = params["-out"]?.let(::File) ?: OutputChannel.None()

    val obstacles = params["-obstacles"]?.let {
        it.split(";")
            .map {
                val rawCoordinates = it.split(",")
                Coordinates(rawCoordinates[0].replace("[", "").toInt(), rawCoordinates[1].replace("]", "").toInt())
            }
    }
        ?.let(::Obstacles)
        ?: Obstacles(emptyList())

    val mapBoundaries = params["-map"]?.let {
        val mapRegex = """^(\d)+x(\d)+,?(w)?$""".toRegex()
        val (_, width, height, type) = mapRegex.find(it)!!.groupValues
        when (type) {
            "" -> ::Boundaries
            "w" -> ::Wrap
            else -> TODO("Unrecognized map type")
        }(width.toInt(), height.toInt())
    } ?: MapPlugin.None

    val map = Map {
        apply(mapBoundaries)
        apply(obstacles)
    }

    val outputChannel = StdOut() + outputFile

    // todo better domain layer
    val parseCommand = CascadingParseCommand(
        ParseSimpleStringCommand,
        ParseAtomicStringCommand
    )

    val input = params["-command"]!!

    val executeCommand =
        ExecuteSingleCommand(parseCommand, map)
            .reportingWith(
                ReportCommandExecutedAsString(ReportRoverPositionAsString),
                ReportErrorAsString(ReportRoverPositionAsString),
                outputChannel
            )
            .handlingMultipleCommands(SplitBy(","))

    executeCommand(initialRover, input)

    outputChannel.flush()
}

class SplitBy(private val separator: String) : (String) -> Iterable<String> {
    override fun invoke(value: String): Iterable<String> =
        value.split(separator)
}

private fun String.toDirection() =
    when (this) {
        "n" -> North
        "e" -> East
        "s" -> South
        "w" -> West
        else -> TODO("Direction not recognized")
    }
