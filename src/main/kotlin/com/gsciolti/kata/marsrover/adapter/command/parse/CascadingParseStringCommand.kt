package com.gsciolti.kata.marsrover.adapter.command.parse

import com.gsciolti.kata.marsrover.domain.command.AtomicCommand
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.MoveBackward
import com.gsciolti.kata.marsrover.domain.command.MoveForward
import com.gsciolti.kata.marsrover.domain.command.TurnLeft
import com.gsciolti.kata.marsrover.domain.command.TurnRight
import com.gsciolti.kata.marsrover.domain.command.execute.error.CommandNotValid
import com.gsciolti.kata.marsrover.domain.command.parse.ParseCommand
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMapLeft
import com.gsciolti.kata.marsrover.functional.left
import com.gsciolti.kata.marsrover.functional.right
import com.gsciolti.kata.marsrover.functional.update

typealias StringCommandNotValid = CommandNotValid<String>

object ParseSimpleStringCommand : ParseCommand<String> {

    override fun invoke(command: String): Either<StringCommandNotValid, Command> =
        when (command) {
            "f" -> MoveForward.right()
            "b" -> MoveBackward.right()
            "l" -> TurnLeft.right()
            "r" -> TurnRight.right()
            else -> CommandNotValid(command).left()
        }
}

// todo refactor
object ParseAtomicStringCommand : ParseCommand<String> {

    override fun invoke(command: String): Either<StringCommandNotValid, Command> =
        command
            .split("_")
            .update(mutableListOf<Command>()) { commands, nextCommand ->
                ParseSimpleStringCommand(nextCommand)
                    .map { commands.apply { add(it) } }
            }
            .map(::AtomicCommand)
}

class CascadingParseStringCommand<T>(
    private val firstParse: ParseCommand<T>,
    private vararg val others: ParseCommand<T>
) : ParseCommand<T> {

    override fun invoke(command: T): Either<CommandNotValid<T>, Command> =
        others.fold(firstParse(command)) { result, nextParse ->
            result.flatMapLeft { nextParse(command) }
        }
}
