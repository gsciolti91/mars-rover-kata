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
        } ?: OpenMap

    val realObstacles = Obstacles(
        obstacles?.let { it.map { Coordinates(it.first, it.second) } } ?: emptyList()
    )

    val currentPosition = Coordinates(start.split(",")[0].toInt(), start.split(",")[1].toInt())
    val currentDirection = start.split(",")[2].toDirection()

    var rover = Rover(currentPosition, currentDirection)

    val commands = command.split(",")

    for (cmd in commands) {

        val domainCommand = try {
            cmd.toCommand()
        } catch (e: Exception) {
            println("Invalid command '$cmd'. Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            break
        }

        var nextRover =
            when (Pair(domainCommand, rover.facing)) {
                MoveForward to North -> rover.copy(position = rover.position.increaseY())
                MoveForward to East -> rover.copy(position = rover.position.increaseX())
                MoveForward to South -> rover.copy(position = rover.position.decreaseY())
                MoveForward to West -> rover.copy(position = rover.position.decreaseX())
                MoveBackward to North -> rover.copy(position = rover.position.decreaseY())
                MoveBackward to East -> rover.copy(position = rover.position.decreaseX())
                MoveBackward to South -> rover.copy(position = rover.position.increaseY())
                MoveBackward to West -> rover.copy(position = rover.position.increaseX())
                TurnLeft to North -> rover.copy(facing = rover.facing.left())
                TurnLeft to East -> rover.copy(facing = rover.facing.left())
                TurnLeft to South -> rover.copy(facing = rover.facing.left())
                TurnLeft to West -> rover.copy(facing = rover.facing.left())
                TurnRight to North -> rover.copy(facing = rover.facing.right())
                TurnRight to East -> rover.copy(facing = rover.facing.right())
                TurnRight to South -> rover.copy(facing = rover.facing.right())
                TurnRight to West -> rover.copy(facing = rover.facing.right())
                else -> TODO("Command;direction pair not handled")
            }

        nextRover = nextRover.copy(position = map.adjust(nextRover.position))

        val move = Move(rover.position, nextRover.position)
        var error: Any? = null

        map.validate(move)
            .flatMap { m -> realObstacles.validate(m) }
            .map {
                rover = nextRover.copy()
            }
            .tap {
                val dirmsg = when (domainCommand) {
                    is MoveForward -> "Rover moved forward"
                    is MoveBackward -> "Rover moved backward"
                    is TurnLeft -> "Rover turned left"
                    is TurnRight -> "Rover turned right"
                }

                println("$dirmsg. Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            }
            .tapLeft { e ->
                error = e
                when (e) {
                    is BoundaryEncountered -> println("Boundary encountered at [${e.move.currentPosition.x},${e.move.currentPosition.y}]. Current [${e.move.currentPosition.x},${e.move.currentPosition.y}:${rover.facing.asString()}]")
                    is ObstacleEncountered -> println("Obstacle encountered at [${e.move.nextPosition.x},${e.move.nextPosition.y}]. Current [${e.move.currentPosition.x},${e.move.currentPosition.y}:${rover.facing.asString()}]")
                }
            }

        if (error != null) break
    }
}

data class Rover(val position: Coordinates, val facing: Direction)

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


class WrappingMap(private val width: Int, private val height: Int) : Map {

    override fun adjust(coordinates: Coordinates) =
        when {
            coordinates.x == width -> coordinates.copy(x = 0)
            coordinates.x == -1 -> coordinates.copy(x = width - 1)
            coordinates.y == height -> coordinates.copy(y = 0)
            coordinates.y == -1 -> coordinates.copy(y = height - 1)
            else -> coordinates
        }
}

object OpenMap : Map

sealed class Direction(val left: () -> Direction, val right: () -> Direction) {

    object North : Direction({ West }, { East })
    object East : Direction({ North }, { South })
    object South : Direction({ East }, { West })
    object West : Direction({ South }, { North })
}
