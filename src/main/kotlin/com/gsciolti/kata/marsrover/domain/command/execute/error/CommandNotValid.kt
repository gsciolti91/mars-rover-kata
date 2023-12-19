package com.gsciolti.kata.marsrover.domain.command.execute.error

class CommandNotValid(val rawCommand: String) : ExecuteCommandError()