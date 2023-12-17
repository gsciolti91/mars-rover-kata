package com.gsciolti.kata.marsrover.domain.command.execute

import com.gsciolti.kata.marsrover.and
import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.parse.ParseCommand
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMap

class ExecuteCommandApi<IN>(
    private val parseCommand: ParseCommand<IN>,
    private val map: Map
) : ExecuteCommand<IN> {

    override fun invoke(rover: Rover, rawCommand: IN): Either<Any, Pair<Command, Rover>> =
        parseCommand(rawCommand)
            .map { command: Command -> command and command.apply(rover) }
            .flatMap { (command, move) ->
                map.validate(move)
                    .map { rover -> command and rover }
            }
}