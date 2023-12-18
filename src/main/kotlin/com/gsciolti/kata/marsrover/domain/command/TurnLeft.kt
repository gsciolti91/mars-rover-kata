package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Rover

object TurnLeft : Command {
    override fun apply(rover: Rover) =
        Move(rover, rover.copy(facing = rover.facing.left()))
}