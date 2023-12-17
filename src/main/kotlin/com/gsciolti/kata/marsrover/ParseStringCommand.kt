package com.gsciolti.kata.marsrover

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.MoveBackward
import com.gsciolti.kata.marsrover.domain.command.MoveForward
import com.gsciolti.kata.marsrover.domain.command.TurnLeft
import com.gsciolti.kata.marsrover.domain.command.TurnRight
import com.gsciolti.kata.marsrover.domain.command.parse.CommandNotValid
import com.gsciolti.kata.marsrover.domain.command.parse.ParseCommand
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.left
import com.gsciolti.kata.marsrover.functional.right

object ParseStringCommand : ParseCommand<String> {
    override fun invoke(command: String): Either<CommandNotValid, Command> =
        when (command) {
            "f" -> MoveForward.right()
            "b" -> MoveBackward.right()
            "l" -> TurnLeft.right()
            "r" -> TurnRight.right()
            else -> CommandNotValid(command).left()
        }
}