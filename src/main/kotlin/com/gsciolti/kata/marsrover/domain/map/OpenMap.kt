package com.gsciolti.kata.marsrover.domain.map

import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Obstacles
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either

class OpenMap(obstacles: Obstacles) : Map(obstacles) {

    override fun validate(move: Move): Either<Any, Rover> =
        obstacles.validate(move).map { it.nextRover }
}