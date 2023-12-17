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

    val start = args.filter { it.startsWith("-start") }.first().split("=")[1]
    val commandParam = args.filter { it.startsWith("-command") }.first().split("=")[1]
    val obstacles: List<Pair<Int, Int>>? = args
        .filter { it.startsWith("-obstacles") }
        .getOrNull(0)
        ?.let {
            it.split("=")[1]
                .split(";")
                .map {
                    val rawCoordinates = it.split(",")
                    rawCoordinates[0].replace("[", "").toInt() to rawCoordinates[1].replace("]", "").toInt()
                }
        }

    val realObstacles = Obstacles(
        obstacles?.let { it.map { Coordinates(it.first, it.second) } } ?: emptyList()
    )

    val map = args
        .filter { it.startsWith("-map") }
        .getOrNull(0)
        ?.let {
            val rawMapAndType = it.split("=")[1].split(",")
            val mapDimensions = rawMapAndType[0].split("x")
            val mapType = rawMapAndType.getOrElse(1) { "" }

            when (mapType) {
                "" -> BoundedMap(mapDimensions[0].toInt(), mapDimensions[1].toInt(), realObstacles)
                "w" -> WrappingMap(mapDimensions[0].toInt(), mapDimensions[1].toInt(), realObstacles)
                else -> TODO("Unrecognized map type")
            }
        } ?: OpenMap(realObstacles)

    val executeCommand =
        ExecuteCommandApi(ParseStringCommand, map)
            .reportingWith(
                ReportRoverPositionAsString,
                ReportCommandExecutedAsString,
                ReportErrorAsString,
                StdOut
            )

    val initialPosition = Coordinates(start.split(",")[0].toInt(), start.split(",")[1].toInt())
    val initialDirection = start.split(",")[2].toDirection()

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
