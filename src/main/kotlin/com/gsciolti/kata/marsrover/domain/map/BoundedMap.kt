package com.gsciolti.kata.marsrover.domain.map

import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Obstacles
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMap
import com.gsciolti.kata.marsrover.functional.left
import com.gsciolti.kata.marsrover.functional.right

class BoundedMap(private val width: Int, private val height: Int, obstacles: Obstacles) : Map(obstacles) {

    override fun validate(move: Move): Either<Any, Rover> =
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