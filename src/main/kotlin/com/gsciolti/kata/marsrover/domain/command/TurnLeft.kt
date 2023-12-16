package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.Move
import com.gsciolti.kata.marsrover.domain.Rover

object TurnLeft : Command {
    override fun apply(rover: Rover) =
        Move(rover, rover.copy(facing = rover.facing.left()))
}