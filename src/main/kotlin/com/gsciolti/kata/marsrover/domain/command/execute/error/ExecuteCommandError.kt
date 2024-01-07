package com.gsciolti.kata.marsrover.domain.command.execute.error

import com.gsciolti.kata.marsrover.domain.model.Rover

sealed class ExecuteCommandError(lastValidRover: Rover) : MarsRoverError(lastValidRover)