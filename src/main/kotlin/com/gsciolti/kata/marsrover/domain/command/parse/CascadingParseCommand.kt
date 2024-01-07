package com.gsciolti.kata.marsrover.domain.command.parse

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.flatMapLeft

class CascadingParseCommand<T>(
    private val firstParse: ParseCommand<T>,
    private vararg val others: ParseCommand<T>
) : ParseCommand<T> {

    override fun invoke(command: T): Either<ParseCommandError<T>, Command> =
        others.fold(firstParse(command)) { result, nextParse ->
            result.flatMapLeft { nextParse(command) }
        }
}