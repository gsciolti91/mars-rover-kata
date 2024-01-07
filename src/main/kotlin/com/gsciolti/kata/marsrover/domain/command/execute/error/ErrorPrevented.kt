package com.gsciolti.kata.marsrover.domain.command.execute.error

import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.model.Rover

class ErrorPrevented(val error: ExecuteCommandError, lastValidRover: Rover) : ExecuteCommandError(lastValidRover)