package com.gsciolti.kata.marsrover.domain.command.execute.error

// todo simple error
class ErrorPrevented(val error: ExecuteCommandError) : ExecuteCommandError()