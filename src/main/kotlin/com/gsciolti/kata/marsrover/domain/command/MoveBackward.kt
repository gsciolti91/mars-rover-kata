package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.Direction
import com.gsciolti.kata.marsrover.domain.Move
import com.gsciolti.kata.marsrover.domain.Rover

object MoveBackward : Command {
    override fun apply(rover: Rover) =
        when (rover.facing) {
            Direction.North -> Move(rover, rover.copy(position = rover.position.decreaseY()))
            Direction.East -> Move(rover, rover.copy(position = rover.position.decreaseX()))
            Direction.South -> Move(rover, rover.copy(position = rover.position.increaseY()))
            Direction.West -> Move(rover, rover.copy(position = rover.position.increaseX()))
        }
}