package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.and
import com.gsciolti.kata.marsrover.functional.right

object TurnRight : Command {

    override fun apply(rover: Rover, map: Map): Either<ExecuteCommandError, Pair<Command, Rover>> =
        (this and rover.copy(facing = rover.facing.right())).right()
}