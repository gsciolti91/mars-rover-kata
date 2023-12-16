package com.gsciolti.kata.marsrover

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

    var rover = Rover(currentPosition, currentDirection)

    val commands = command.split(",")

    for (cmd in commands) {

        val domainCommand = try {
            cmd.toCommand()
        } catch (e: Exception) {
            println("Invalid command '$cmd'. Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            break
        }

        val move = domainCommand.apply(rover)

        var error: Any? = null

        map.validate(move)
            .map {
                rover = it
            }
            .tap {
                val dirmsg = when (domainCommand) {
                    is MoveForward -> "Rover moved forward"
                    is MoveBackward -> "Rover moved backward"
                    is TurnLeft -> "Rover turned left"
                    is TurnRight -> "Rover turned right"
                    else -> TODO("Command not recognized")
                }

                println("$dirmsg. Current [${rover.position.x},${rover.position.y}:${rover.facing.asString()}]")
            }
            .tapLeft { e ->
                error = e
                when (e) {
                    is BoundaryEncountered -> println("Boundary encountered at [${e.move.currentRover.position.x},${e.move.currentRover.position.y}]. Current [${e.move.currentRover.position.x},${e.move.currentRover.position.y}:${rover.facing.asString()}]")
                    is ObstacleEncountered -> println("Obstacle encountered at [${e.move.nextRover.position.x},${e.move.nextRover.position.y}]. Current [${e.move.currentRover.position.x},${e.move.currentRover.position.y}:${rover.facing.asString()}]")
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

interface Command {
    fun apply(rover: Rover): Move
}

object MoveForward : Command {
    override fun apply(rover: Rover) =
        when (rover.facing) {
            North -> Move(rover, rover.copy(position = rover.position.increaseY()))
            East -> Move(rover, rover.copy(position = rover.position.increaseX()))
            South -> Move(rover, rover.copy(position = rover.position.decreaseY()))
            West -> Move(rover, rover.copy(position = rover.position.decreaseX()))
        }
}

object MoveBackward : Command {
    override fun apply(rover: Rover) =
        when (rover.facing) {
            North -> Move(rover, rover.copy(position = rover.position.decreaseY()))
            East -> Move(rover, rover.copy(position = rover.position.decreaseX()))
            South -> Move(rover, rover.copy(position = rover.position.increaseY()))
            West -> Move(rover, rover.copy(position = rover.position.increaseX()))
        }
}

object TurnLeft : Command {
    override fun apply(rover: Rover) =
        Move(rover, rover.copy(facing = rover.facing.left()))
}

object TurnRight : Command {
    override fun apply(rover: Rover) =
        Move(rover, rover.copy(facing = rover.facing.right()))
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
        if (coordinates.contains(move.nextRover.position))
            ObstacleEncountered(move).left()
        else
            move.right()
}

data class Move(val currentRover: Rover, val nextRover: Rover)

class ObstacleEncountered(val move: Move)

class BoundaryEncountered(val move: Move)

// todo obstacles, width height as plugins
sealed class Map(protected val obstacles: Obstacles) {

    // todo change any to err
    abstract fun validate(move: Move): Either<Any, Rover>
}


class BoundedMap(private val width: Int, private val height: Int, obstacles: Obstacles) : Map(obstacles) {

    override fun validate(move: Move) =
        validateAgainstBoundaries(move)
            .flatMap { obstacles.validate(move) }
            .map { it.nextRover }

    private fun validateAgainstBoundaries(move: Move) =
        // todo between
        if (move.nextRover.position.x < 0 || move.nextRover.position.x >= width ||
            move.nextRover.position.y < 0 || move.nextRover.position.y >= height
        )
            BoundaryEncountered(move).left()
        else
            move.right()
}


class WrappingMap(private val width: Int, private val height: Int, obstacles: Obstacles) : Map(obstacles) {

    override fun validate(move: Move): Either<Any, Rover> {

        val adjustedMove = move.copy(nextRover = move.nextRover.copy(position = adjust(move.nextRover.position)))

        return obstacles.validate(adjustedMove)
            .map { it.nextRover }
    }

    private fun adjust(coordinates: Coordinates) =
        when {
            coordinates.x == width -> coordinates.copy(x = 0)
            coordinates.x == -1 -> coordinates.copy(x = width - 1)
            coordinates.y == height -> coordinates.copy(y = 0)
            coordinates.y == -1 -> coordinates.copy(y = height - 1)
            else -> coordinates
        }
}

class OpenMap(obstacles: Obstacles) : Map(obstacles) {
    override fun validate(move: Move): Either<Any, Rover> {
        return obstacles.validate(move).map { it.nextRover }
    }
}

sealed class Direction(val left: () -> Direction, val right: () -> Direction) {

    object North : Direction({ West }, { East })
    object East : Direction({ North }, { South })
    object South : Direction({ East }, { West })
    object West : Direction({ South }, { North })
}
