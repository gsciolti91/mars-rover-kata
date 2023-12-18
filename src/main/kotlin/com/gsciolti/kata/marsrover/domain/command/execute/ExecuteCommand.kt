package com.gsciolti.kata.marsrover.domain.command.execute

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either

interface ExecuteCommand<IN> : (Rover, IN) -> Either<Any, Pair<Command, Rover>>