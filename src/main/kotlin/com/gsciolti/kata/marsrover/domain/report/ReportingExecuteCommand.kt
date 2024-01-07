package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteCommand
import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.andThen

class ReportingExecuteCommand<IN, OUT> internal constructor(
    private val delegateExecute: ExecuteCommand<IN>,
    reportCommandExecuted: ReportCommandExecuted<OUT>,
    reportError: ReportError<OUT>,
    outputChannel: OutputChannel<OUT>,
) : ExecuteCommand<IN> {

    override fun invoke(rover: Rover, command: IN): Either<MarsRoverError, Pair<Command, Rover>> =
        delegateExecute(rover, command)
            .tap { (command, updatedRover) -> displayCommandExecuted(command, updatedRover) }
            .tapLeft(displayError)

    private val displayCommandExecuted: (Command, Rover) -> Unit =
        reportCommandExecuted andThen outputChannel::display

    private val displayError: (MarsRoverError) -> Unit =
        reportError andThen outputChannel::display
}

fun <IN, OUT> ExecuteCommand<IN>.reportingWith(
    reportCommandExecuted: ReportCommandExecuted<OUT>,
    reportError: ReportError<OUT>,
    outputChannel: OutputChannel<OUT>
) =
    ReportingExecuteCommand(
        this,
        reportCommandExecuted,
        reportError,
        outputChannel
    )
