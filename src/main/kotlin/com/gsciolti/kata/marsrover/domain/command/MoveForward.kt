package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.Direction.East
import com.gsciolti.kata.marsrover.domain.Direction.North
import com.gsciolti.kata.marsrover.domain.Direction.South
import com.gsciolti.kata.marsrover.domain.Direction.West
import com.gsciolti.kata.marsrover.domain.Move
import com.gsciolti.kata.marsrover.domain.Rover

object MoveForward : Command {
    override fun apply(rover: Rover) =
        when (rover.facing) {
            North -> Move(rover, rover.copy(position = rover.position.increaseY()))
            East -> Move(rover, rover.copy(position = rover.position.increaseX()))
            South -> Move(rover, rover.copy(position = rover.position.decreaseY()))
            West -> Move(rover, rover.copy(position = rover.position.decreaseX()))
        }
}