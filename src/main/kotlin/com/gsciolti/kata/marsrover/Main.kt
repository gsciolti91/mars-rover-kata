package com.gsciolti.kata.marsrover

import com.gsciolti.kata.marsrover.domain.Coordinates
import com.gsciolti.kata.marsrover.domain.Direction
import com.gsciolti.kata.marsrover.domain.Direction.East
import com.gsciolti.kata.marsrover.domain.Direction.North
import com.gsciolti.kata.marsrover.domain.Direction.South
import com.gsciolti.kata.marsrover.domain.Direction.West
import com.gsciolti.kata.marsrover.domain.Obstacles
import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.MoveBackward
import com.gsciolti.kata.marsrover.domain.command.MoveForward
import com.gsciolti.kata.marsrover.domain.command.TurnLeft
import com.gsciolti.kata.marsrover.domain.command.TurnRight
import com.gsciolti.kata.marsrover.domain.command.parse.CommandNotValid
import com.gsciolti.kata.marsrover.domain.map.BoundedMap
import com.gsciolti.kata.marsrover.domain.map.Map.BoundaryEncountered
import com.gsciolti.kata.marsrover.domain.map.Map.ObstacleEncountered
import com.gsciolti.kata.marsrover.domain.map.OpenMap
import com.gsciolti.kata.marsrover.domain.map.WrappingMap
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMap
import com.gsciolti.kata.marsrover.functional.right

fun main(vararg args: String) {

    val start = args.filter { it.startsWith("-start") }.first().split("=")[1]
    val command = args.filter { it.startsWith("-command") }.first().split("=")[1]
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

    val currentPosition = Coordinates(start.split(",")[0].toInt(), start.split(",")[1].toInt())
    val currentDirection = start.split(",")[2].toDirection()

    val commands = command.split(",")

    val inputs: Iterable<String> = commands
    val initialState = Rover(currentPosition, currentDirection)
    val update: (Rover, String) -> Either<Any, Rover> = { rover, rawCommand ->

        // todo extract api and decoration
        // domain
        ParseStringCommand(rawCommand)
            .map { command: Command -> command and command.apply(rover) }
            .flatMap { (command, move) ->
                map.validate(move)
                    .map { rover -> command and rover }
            }

            // logging
            .tap { (command, rover) ->
                val commandMsg = ReportCommandExecutedAsString(command)
                val roverMsg = ReportRoverPositionAsString(rover)

                println((commandMsg + roverMsg).value)
            }
            .tapLeft { e ->

                val reportError = ReportErrorAsString

                when (e) {
                    is CommandNotValid -> {
                        val roverMsg = ReportRoverPositionAsString(rover)
                        val errorMsg = reportError(e) // todo e.report() possible??
                        println((errorMsg + roverMsg).value)
                    }

                    is BoundaryEncountered -> {
                        val roverMsg = ReportRoverPositionAsString(e.move.currentRover)
                        val errorMsg = reportError(e)
                        println((errorMsg + roverMsg).value)
                    }

                    is ObstacleEncountered -> {
                        val roverMsg = ReportRoverPositionAsString(e.move.currentRover)
                        val errorMsg = reportError(e)
                        println((errorMsg + roverMsg).value)
                    }
                }
            }

            // flow
            .map { (_, rover) -> rover }
    }

    inputs.fold(initialState.right() as Either<Any, Rover>) { currentState, nextInput ->
        currentState.flatMap { state -> update(state, nextInput) }
    }
}

abstract class Output<T>(val value: T) {

    abstract operator fun plus(other: Output<T>): Output<T>
}

class StringOutput(value: String) : Output<String>(value) {

    override operator fun plus(other: Output<String>): Output<String> =
        StringOutput("$value. ${other.value}")
}

interface ReportCommandExecuted<T> : (Command) -> Output<T>

object ReportCommandExecutedAsString : ReportCommandExecuted<String> {

    override fun invoke(command: Command): StringOutput =
        when (command) {
            is MoveForward -> StringOutput("Rover moved forward")
            is MoveBackward -> StringOutput("Rover moved backward")
            is TurnLeft -> StringOutput("Rover turned left")
            is TurnRight -> StringOutput("Rover turned right")
        }
}

interface ReportError<T> : (Any) -> Output<T>

object ReportErrorAsString : ReportError<String> {

    override fun invoke(error: Any): StringOutput =
        when (error) {
            is CommandNotValid -> StringOutput("Invalid command '${error.rawCommand}'")
            is BoundaryEncountered -> StringOutput("Boundary encountered at [${error.move.currentRover.position.x},${error.move.currentRover.position.y}]")
            is ObstacleEncountered -> StringOutput("Obstacle encountered at [${error.move.nextRover.position.x},${error.move.nextRover.position.y}]")
            else -> TODO("error not handled")
        }
}

interface ReportRoverPosition<T> : (Rover) -> Output<T>

object ReportRoverPositionAsString : ReportRoverPosition<String> {
    override fun invoke(rover: Rover): StringOutput =
        StringOutput("Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
}

private fun Direction.asString() =
    when (this) {
        North -> "n"
        East -> "e"
        South -> "s"
        West -> "w"
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
