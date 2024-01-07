package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either

sealed interface Command {

    fun apply(rover: Rover, map: Map): Either<ExecuteCommandError, Pair<Command, Rover>>
}