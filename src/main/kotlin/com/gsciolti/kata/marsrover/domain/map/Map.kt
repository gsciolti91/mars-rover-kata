package com.gsciolti.kata.marsrover.domain.map

import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Obstacles
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either

// todo obstacles, width height as plugins
sealed class Map(protected val obstacles: Obstacles) {

    // todo change any to err
    // todo use error codes so each map can declare new errors (error as interface?)
    abstract fun validate(move: Move): Either<Any, Rover>

    class ObstacleEncountered(val move: Move)

    class BoundaryEncountered(val move: Move)
}