package com.gsciolti.kata.marsrover.domain.map

import com.gsciolti.kata.marsrover.domain.Move
import com.gsciolti.kata.marsrover.domain.Obstacles
import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.functional.Either

class OpenMap(obstacles: Obstacles) : Map(obstacles) {

    override fun validate(move: Move): Either<Any, Rover> =
        obstacles.validate(move).map { it.nextRover }
}