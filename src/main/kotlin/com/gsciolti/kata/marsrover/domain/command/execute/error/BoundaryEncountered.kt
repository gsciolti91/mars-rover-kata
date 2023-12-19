package com.gsciolti.kata.marsrover.domain.command.execute.error

import com.gsciolti.kata.marsrover.domain.model.Move

class BoundaryEncountered(val move: Move) : ExecuteCommandError()