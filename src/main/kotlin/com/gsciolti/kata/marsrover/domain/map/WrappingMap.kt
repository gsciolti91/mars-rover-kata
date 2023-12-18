package com.gsciolti.kata.marsrover.domain.map

import com.gsciolti.kata.marsrover.domain.model.Coordinates
import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Obstacles
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either

class WrappingMap(private val width: Int, private val height: Int, obstacles: Obstacles) : Map(obstacles) {

    override fun validate(move: Move): Either<Any, Rover> {

        val adjustedMove = move.copy(nextRover = move.nextRover.copy(position = adjust(move.nextRover.position)))

        return obstacles
            .validate(adjustedMove)
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