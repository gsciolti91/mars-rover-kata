package com.gsciolti.kata.marsrover.domain.map

import com.gsciolti.kata.marsrover.domain.command.execute.error.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Obstacles
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either

// todo obstacles, width height as plugins
sealed class Map(protected val obstacles: Obstacles) {

    abstract fun validate(move: Move): Either<ExecuteCommandError, Rover>
}