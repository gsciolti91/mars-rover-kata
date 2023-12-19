package com.gsciolti.kata.marsrover.domain.command.execute.error

import com.gsciolti.kata.marsrover.domain.model.Move

class ObstacleEncountered(val move: Move) : ExecuteCommandError()