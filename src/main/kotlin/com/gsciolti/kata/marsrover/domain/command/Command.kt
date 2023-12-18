package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Rover

sealed interface Command {
    fun apply(rover: Rover): Move
}