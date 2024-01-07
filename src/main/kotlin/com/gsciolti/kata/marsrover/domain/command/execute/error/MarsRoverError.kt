package com.gsciolti.kata.marsrover.domain.command.execute.error

import com.gsciolti.kata.marsrover.domain.model.Rover

sealed class MarsRoverError(val lastValidRover: Rover) {

    class CommandNotValid<T>(val rawCommand: T, lastValidRover: Rover) : MarsRoverError(lastValidRover)

    sealed class ExecuteCommandError(lastValidRover: Rover) : MarsRoverError(lastValidRover)
}