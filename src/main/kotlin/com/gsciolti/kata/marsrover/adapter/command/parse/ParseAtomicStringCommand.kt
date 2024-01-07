package com.gsciolti.kata.marsrover.adapter.command.parse

import com.gsciolti.kata.marsrover.domain.command.AtomicCommand
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.parse.ParseCommand
import com.gsciolti.kata.marsrover.domain.command.parse.ParseCommandError
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.update

object ParseAtomicStringCommand : ParseCommand<String> {

    override fun invoke(command: String): Either<ParseCommandError<String>, Command> =
        command
            .split("_")
            .update(mutableListOf<Command>()) { commands, nextCommand ->
                // todo extract?
                ParseSimpleStringCommand(nextCommand)
                    .map { commands.apply { add(it) } }
            }
            .map(::AtomicCommand)
}
