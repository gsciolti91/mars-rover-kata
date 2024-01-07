package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.model.Direction.East
import com.gsciolti.kata.marsrover.domain.model.Direction.North
import com.gsciolti.kata.marsrover.domain.model.Direction.South
import com.gsciolti.kata.marsrover.domain.model.Direction.West
import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.and

object MoveBackward : Command {

    override fun apply(rover: Rover, map: Map): Either<ExecuteCommandError, Pair<Command, Rover>> {
        val move = when (rover.facing) {
            North -> Move(rover, rover.copy(position = rover.position.decreaseY()))
            East -> Move(rover, rover.copy(position = rover.position.decreaseX()))
            South -> Move(rover, rover.copy(position = rover.position.increaseY()))
            West -> Move(rover, rover.copy(position = rover.position.increaseX()))
        }

        return map
            .validate(move)
            .map { rover -> this and rover }
    }
}