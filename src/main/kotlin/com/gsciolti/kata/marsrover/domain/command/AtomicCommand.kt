package com.gsciolti.kata.marsrover.domain.command

import com.gsciolti.kata.marsrover.domain.command.execute.error.ErrorPrevented
import com.gsciolti.kata.marsrover.domain.command.execute.error.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.and
import com.gsciolti.kata.marsrover.functional.update

class AtomicCommand(private val commands: List<Command>) : Command {

    private val commandsExecuted = mutableListOf<Pair<Command, Rover>>()

    override fun apply(rover: Rover): Move {
        TODO("Not yet implemented")
    }

    override fun apply(rover: Rover, map: Map): Either<ExecuteCommandError, Pair<Command, Rover>> =
        commands
            .update(rover) { currentRover, nextCommand ->
                nextCommand
                    .apply(currentRover, map)
                    .tap(commandsExecuted::add)
                    .map { (_, updatedRover) -> updatedRover }
            }
            .map { finalRover -> this and finalRover }
            .mapLeft(::ErrorPrevented)

    fun commandsExecuted(): List<Pair<Command, Rover>> = commandsExecuted
}