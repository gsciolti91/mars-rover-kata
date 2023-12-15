package com.gsciolti.kata.marsrover

import com.gsciolti.kata.marsrover.Command.MoveBackward
import com.gsciolti.kata.marsrover.Command.MoveForward
import com.gsciolti.kata.marsrover.Command.TurnLeft
import com.gsciolti.kata.marsrover.Command.TurnRight
import com.gsciolti.kata.marsrover.Direction.East
import com.gsciolti.kata.marsrover.Direction.North
import com.gsciolti.kata.marsrover.Direction.South
import com.gsciolti.kata.marsrover.Direction.West
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMap
import com.gsciolti.kata.marsrover.functional.left
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

    val map = args
        .filter { it.startsWith("-map") }
        .getOrNull(0)
        ?.let {
            val rawMapAndType = it.split("=")[1].split(",")
            val mapDimensions = rawMapAndType[0].split("x")
            val mapType = rawMapAndType.getOrElse(1) { "" }

            when (mapType) {
                "" -> BoundedMap(mapDimensions[0].toInt(), mapDimensions[1].toInt())
                "w" -> WrappingMap(mapDimensions[0].toInt(), mapDimensions[1].toInt())
                else -> TODO("Unrecognized map type")
            }
        } ?: OpenWorld

    val realObstacles = Obstacles(
        obstacles?.let { it.map { Coordinates(it.first, it.second) } } ?: emptyList()
    )

    var currentPosition = Coordinates(start.split(",")[0].toInt(), start.split(",")[1].toInt())
    var currentDirection = start.split(",")[2].toDirection()

    val commands = command.split(",")

    for (cmd in commands) {

        val domainCommand = try {
            cmd.toCommand()
        } catch (e: Exception) {
            println("Invalid command '$cmd'. Current [${currentPosition.x},${currentPosition.y}:${currentDirection.asString()}]")
            break
        }

        val (nextPosition, nextDirection) =
            when (Pair(domainCommand, currentDirection)) {
                MoveForward to North -> map.adjust(currentPosition.increaseY()) to currentDirection
                MoveForward to East -> map.adjust(currentPosition.increaseX()) to currentDirection
                MoveForward to South -> map.adjust(currentPosition.decreaseY()) to currentDirection
                MoveForward to West -> map.adjust(currentPosition.decreaseX()) to currentDirection
                MoveBackward to North -> map.adjust(currentPosition.decreaseY()) to currentDirection
                MoveBackward to East -> map.adjust(currentPosition.decreaseX()) to currentDirection
                MoveBackward to South -> map.adjust(currentPosition.increaseY()) to currentDirection
                MoveBackward to West -> map.adjust(currentPosition.increaseX()) to currentDirection
                TurnLeft to North -> currentPosition to currentDirection.left()
                TurnLeft to East -> currentPosition to currentDirection.left()
                TurnLeft to South -> currentPosition to currentDirection.left()
                TurnLeft to West -> currentPosition to currentDirection.left()
                TurnRight to North -> currentPosition to currentDirection.right()
                TurnRight to East -> currentPosition to currentDirection.right()
                TurnRight to South -> currentPosition to currentDirection.right()
                TurnRight to West -> currentPosition to currentDirection.right()
                else -> TODO("Command;direction pair not handled")
            }

        val move = Move(currentPosition, nextPosition)
        var error: Any? = null

        map.validate(move)
            .flatMap { m -> realObstacles.validate(m) }
            .map {
                currentPosition = nextPosition.copy()
                currentDirection = nextDirection
            }
            .tap {
                val dirmsg = when (domainCommand) {
                    is MoveForward -> "Rover moved forward"
                    is MoveBackward -> "Rover moved backward"
                    is TurnLeft -> "Rover turned left"
                    is TurnRight -> "Rover turned right"
                }

                println("$dirmsg. Current [${nextPosition.x},${nextPosition.y}:${nextDirection.asString()}]")
            }
            .tapLeft { e ->
                error = e
                when (e) {
                    is BoundaryEncountered -> println("Boundary encountered at [${e.move.currentPosition.x},${e.move.currentPosition.y}]. Current [${e.move.currentPosition.x},${e.move.currentPosition.y}:${currentDirection.asString()}]")
                    is ObstacleEncountered -> println("Obstacle encountered at [${e.move.nextPosition.x},${e.move.nextPosition.y}]. Current [${e.move.currentPosition.x},${e.move.currentPosition.y}:${currentDirection.asString()}]")
                }
            }

        if (error != null) break
    }
}

private fun String.toCommand() =
    when (this) {
        "f" -> MoveForward
        "b" -> MoveBackward
        "l" -> TurnLeft
        "r" -> TurnRight
        else -> throw IllegalArgumentException("Command not recognized")
    }

sealed class Command {
    object MoveForward : Command()
    object MoveBackward : Command()
    object TurnLeft : Command()
    object TurnRight : Command()
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

class Obstacles(private val coordinates: List<Coordinates>) {

    fun validate(move: Move) =
        if (coordinates.contains(move.nextPosition))
            ObstacleEncountered(move).left()
        else
            move.right()
}

data class Move(val currentPosition: Coordinates, val nextPosition: Coordinates)

class ObstacleEncountered(val move: Move)

class BoundaryEncountered(val move: Move)

interface Map {

    fun adjust(coordinates: Coordinates): Coordinates =
        coordinates

    fun validate(move: Move): Either<BoundaryEncountered, Move> =
        move.right()
}


class BoundedMap(private val width: Int, private val height: Int) : Map {

    override fun validate(move: Move) =
        if (move.nextPosition.x < 0 || move.nextPosition.x >= width ||
            move.nextPosition.y < 0 || move.nextPosition.y >= height
        )
            BoundaryEncountered(move).left()
        else
            move.right()
}


class WrappingMap(val width: Int, val height: Int) : Map {

    override fun adjust(coordinates: Coordinates) =
        when {
            coordinates.x == width -> coordinates.copy(x = 0)
            coordinates.x == -1 -> coordinates.copy(x = width - 1)
            coordinates.y == height -> coordinates.copy(y = 0)
            coordinates.y == -1 -> coordinates.copy(y = height - 1)
            else -> coordinates
        }
}

object OpenWorld : Map

sealed class Direction(val left: () -> Direction, val right: () -> Direction) {

    object North : Direction({ West }, { East })
    object East : Direction({ North }, { South })
    object South : Direction({ East }, { West })
    object West : Direction({ South }, { North })
}
