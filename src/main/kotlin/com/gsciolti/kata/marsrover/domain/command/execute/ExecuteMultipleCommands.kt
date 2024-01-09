package com.gsciolti.kata.marsrover.domain.command.execute

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMap

class ExecuteMultipleCommands<IN>(
    private val splitCommands: (IN) -> Iterable<IN>,
    private val delegateExecute: ExecuteCommand<IN>
) : ExecuteCommand<IN> {

    // todo improve
    override fun invoke(rover: Rover, rawCommand: IN): Either<MarsRoverError, Pair<Command, Rover>> {
        val commands = splitCommands(rawCommand)

        val f: (IN) -> (Rover) -> Either<MarsRoverError, Pair<Command, Rover>> =
            { command: IN ->
                { rover: Rover ->
                    delegateExecute(rover, command)
                }
            }

        val execs: List<(Rover) -> Either<MarsRoverError, Pair<Command, Rover>>> = commands.map(f)

        val first = execs.first()
        val others = execs.takeLast(execs.size - 1)

        return others.fold(first(rover)) { current, executeNext -> current.flatMap { (_, rover) -> executeNext(rover) } }
    }
}

fun <IN> ExecuteCommand<IN>.handlingMultipleCommands(splitCommands: (IN) -> Iterable<IN>) =
    ExecuteMultipleCommands(splitCommands, this)