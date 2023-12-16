package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.Move
import com.gsciolti.kata.marsrover.domain.Rover

interface Command {
    fun apply(rover: Rover): Move
}