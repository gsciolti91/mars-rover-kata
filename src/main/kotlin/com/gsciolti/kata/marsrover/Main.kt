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
        // domain
        ParseStringCommand(rawCommand)
            .map { command: Command -> command and command.apply(rover) }
            .flatMap { (command, move) ->
                map.validate(move)
                    .map { rover -> command and rover }
            }

            // logging
            .tap { (command, rover) ->
                val dirmsg = when (command) {
                    is MoveForward -> "Rover moved forward"
                    is MoveBackward -> "Rover moved backward"
                    is TurnLeft -> "Rover turned left"
                    is TurnRight -> "Rover turned right"
                }

                println("$dirmsg. Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            }
            .tapLeft { e ->
                when (e) {
                    // todo display message
                    // todo print rover, not internals
                    is CommandNotValid -> println("Invalid command '${e.rawCommand}'. Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
                    is BoundaryEncountered -> println("Boundary encountered at [${e.move.currentRover.position.x},${e.move.currentRover.position.y}]. Current [${e.move.currentRover.position.x},${e.move.currentRover.position.y}:${rover.facing.asString()}]")
                    is ObstacleEncountered -> println("Obstacle encountered at [${e.move.nextRover.position.x},${e.move.nextRover.position.y}]. Current [${e.move.currentRover.position.x},${e.move.currentRover.position.y}:${rover.facing.asString()}]")
                }
            }

            // flow
            .map { (_, rover) -> rover }
    }

    inputs.fold(initialState.right() as Either<Any, Rover>) { currentState, nextInput ->
        currentState.flatMap { state -> update(state, nextInput) }
    }
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
