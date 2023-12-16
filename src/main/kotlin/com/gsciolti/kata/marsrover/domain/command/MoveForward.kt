package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.Direction
import com.gsciolti.kata.marsrover.domain.Move
import com.gsciolti.kata.marsrover.domain.Rover

object MoveForward : Command {
    override fun apply(rover: Rover) =
        when (rover.facing) {
            Direction.North -> Move(rover, rover.copy(position = rover.position.increaseY()))
            Direction.East -> Move(rover, rover.copy(position = rover.position.increaseX()))
            Direction.South -> Move(rover, rover.copy(position = rover.position.decreaseY()))
            Direction.West -> Move(rover, rover.copy(position = rover.position.decreaseX()))
        }
}