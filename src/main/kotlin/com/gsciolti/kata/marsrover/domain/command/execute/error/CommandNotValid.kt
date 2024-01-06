package com.gsciolti.kata.marsrover.domain.command.execute.error

class CommandNotValid<IN>(val rawCommand: IN) : ExecuteCommandError()