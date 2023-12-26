package com.gsciolti.kata.marsrover.domain.command.execute.error

class ErrorPrevented(val error: ExecuteCommandError) : ExecuteCommandError()