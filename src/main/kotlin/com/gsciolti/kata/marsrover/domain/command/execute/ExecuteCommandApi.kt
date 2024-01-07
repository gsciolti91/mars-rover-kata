package com.gsciolti.kata.marsrover.domain.command.execute

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError
import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError.CommandNotValid
import com.gsciolti.kata.marsrover.domain.command.parse.ParseCommand
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMap

class ExecuteCommandApi<IN>(
    private val parseCommand: ParseCommand<IN>,
    private val map: Map
) : ExecuteCommand<IN> {

    override fun invoke(rover: Rover, rawCommand: IN): Either<MarsRoverError, Pair<Command, Rover>> =
        parseCommand(rawCommand)
            .mapLeft { CommandNotValid(it.rawCommand, rover) }
            .flatMap { command: Command -> command.apply(rover, map) }
}