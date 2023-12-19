package com.gsciolti.kata.marsrover.domain.model

import com.gsciolti.kata.marsrover.domain.command.execute.error.ObstacleEncountered
import com.gsciolti.kata.marsrover.functional.left
import com.gsciolti.kata.marsrover.functional.right

class Obstacles(private val coordinates: List<Coordinates>) {

    fun validate(move: Move) =
        if (coordinates.contains(move.nextRover.position))
            ObstacleEncountered(move).left()
        else
            move.right()
}