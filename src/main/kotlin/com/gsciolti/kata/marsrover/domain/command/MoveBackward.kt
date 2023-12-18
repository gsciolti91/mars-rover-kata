package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.model.Direction.East
import com.gsciolti.kata.marsrover.domain.model.Direction.North
import com.gsciolti.kata.marsrover.domain.model.Direction.South
import com.gsciolti.kata.marsrover.domain.model.Direction.West
import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Rover

object MoveBackward : Command {
    override fun apply(rover: Rover) =
        when (rover.facing) {
            North -> Move(rover, rover.copy(position = rover.position.decreaseY()))
            East -> Move(rover, rover.copy(position = rover.position.decreaseX()))
            South -> Move(rover, rover.copy(position = rover.position.increaseY()))
            West -> Move(rover, rover.copy(position = rover.position.increaseX()))
        }
}