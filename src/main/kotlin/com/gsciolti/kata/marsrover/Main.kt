package com.gsciolti.kata.marsrover

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
    var currentD = start.split(",")[2]

    val nextPosition = currentPosition.copy()
    var newD = currentD

    val commands = command.split(",")

    for (cmd in commands) {
        val dirmsg = when (cmd) {
            "f" -> "Rover moved forward"
            "b" -> "Rover moved backward"
            "l" -> "Rover turned left"
            "r" -> "Rover turned right"
            else -> {
                println("Invalid command '$cmd'. Current [${currentPosition.x},${currentPosition.y}:$currentD]")
                break
            }
        }

        when (Pair(cmd, currentD)) {
            "f" to "n" -> nextPosition.increaseY()
            "f" to "e" -> nextPosition.increaseX()
            "f" to "s" -> nextPosition.decreaseY()
            "f" to "w" -> nextPosition.decreaseX()
            "b" to "n" -> nextPosition.decreaseY()
            "b" to "e" -> nextPosition.decreaseX()
            "b" to "s" -> nextPosition.increaseY()
            "b" to "w" -> nextPosition.increaseX()
            "l" to "n" -> newD = "w"
            "l" to "e" -> newD = "n"
            "l" to "s" -> newD = "e"
            "l" to "w" -> newD = "s"
            "r" to "n" -> newD = "e"
            "r" to "e" -> newD = "s"
            "r" to "s" -> newD = "w"
            "r" to "w" -> newD = "n"
        }

        map.adjust(nextPosition)

        val move = Move(currentPosition, nextPosition)
        var error: Any? = null

        map.validate(move)
            .flatMap { m -> realObstacles.validate(m) }
            .tap {
                println("$dirmsg. Current [${nextPosition.x},${nextPosition.y}:$newD]")

                currentPosition = nextPosition.copy()
                currentD = newD
            }
            .tapLeft { e ->
                error = e
                when (e) {
                    is BoundaryEncountered -> println("Boundary encountered at [${e.move.currentPosition.x},${e.move.currentPosition.y}]. Current [${e.move.currentPosition.x},${e.move.currentPosition.y}:$currentD]")
                    is ObstacleEncountered -> println("Obstacle encountered at [${e.move.nextPosition.x},${e.move.nextPosition.y}]. Current [${e.move.currentPosition.x},${e.move.currentPosition.y}:$currentD]")
                }
            }

        if (error != null) break
    }
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
    override fun adjust(coordinates: Coordinates): Coordinates {
        if (coordinates.x == width) coordinates.x = 0
        if (coordinates.x == -1) coordinates.x = width - 1
        if (coordinates.y == height) coordinates.y = 0
        if (coordinates.y == -1) coordinates.y = height - 1

        return coordinates
    }
}

object OpenWorld : Map {

}
