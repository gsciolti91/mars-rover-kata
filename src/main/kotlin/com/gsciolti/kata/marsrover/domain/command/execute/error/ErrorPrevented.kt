package com.gsciolti.kata.marsrover.domain.command.execute.error

import com.gsciolti.kata.marsrover.domain.model.Rover

// todo simple error
class ErrorPrevented(val error: MarsRoverError, lastValidRover: Rover) : ExecuteCommandError(lastValidRover)