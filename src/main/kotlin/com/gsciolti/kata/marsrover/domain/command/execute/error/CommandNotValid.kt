package com.gsciolti.kata.marsrover.domain.command.execute.error

import com.gsciolti.kata.marsrover.domain.model.Rover

class CommandNotValid<T>(val rawCommand: T, lastValidRover: Rover) : MarsRoverError(lastValidRover)