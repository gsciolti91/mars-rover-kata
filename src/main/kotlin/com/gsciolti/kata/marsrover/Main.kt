package com.gsciolti.kata.marsrover

import com.gsciolti.kata.marsrover.adapter.command.parse.ParseStringCommand
import com.gsciolti.kata.marsrover.adapter.report.ReportCommandExecutedAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportErrorAsString
import com.gsciolti.kata.marsrover.adapter.report.ReportRoverPositionAsString
import com.gsciolti.kata.marsrover.adapter.report.output.StdOut
import com.gsciolti.kata.marsrover.domain.Coordinates
import com.gsciolti.kata.marsrover.domain.Direction.East
import com.gsciolti.kata.marsrover.domain.Direction.North
import com.gsciolti.kata.marsrover.domain.Direction.South
import com.gsciolti.kata.marsrover.domain.Direction.West
import com.gsciolti.kata.marsrover.domain.Obstacles
import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteCommandApi
import com.gsciolti.kata.marsrover.domain.map.BoundedMap
import com.gsciolti.kata.marsrover.domain.map.OpenMap
import com.gsciolti.kata.marsrover.domain.map.WrappingMap
import com.gsciolti.kata.marsrover.domain.report.reportingWith
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMap
import com.gsciolti.kata.marsrover.functional.right

fun main(vararg args: String) {

    val params = args.associate {
        val keyValue = it.split("=")
        keyValue[0] to keyValue[1]
    }

    val startParams = params["-start"]!!.split(",")
    val commandParam = params["-command"]!!
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

    val initialPosition = Coordinates(startParams[0].toInt(), startParams[1].toInt())
    val initialDirection = startParams[2].toDirection()

    val commands: Iterable<String> = commandParam.split(",")

    commands.update(
        initialState = Rover(initialPosition, initialDirection),
        updateState = { rover, command ->
            executeCommand(rover, command)
                .map { (_, rover) -> rover }
        })
}

fun <T, L, R> Iterable<T>.update(initialState: R, updateState: (R, T) -> Either<L, R>): Either<L, R> =
    fold(initialState.right() as Either<L, R>) { currentState, nextInput ->
        currentState.flatMap { state ->
            updateState(state, nextInput)
        }
    }

private fun String.toDirection() =
    when (this) {
        "n" -> North
        "e" -> East
        "s" -> South
        "w" -> West
        else -> TODO("Direction not recognized")
    }

infix fun <A, B> A.and(b: B) = this to b
