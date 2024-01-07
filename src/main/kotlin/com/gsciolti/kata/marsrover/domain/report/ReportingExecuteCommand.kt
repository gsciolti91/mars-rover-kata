package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteCommand
import com.gsciolti.kata.marsrover.domain.command.execute.error.MarsRoverError
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel
import com.gsciolti.kata.marsrover.functional.Either

class ReportingExecuteCommand<CMD, OUT> internal constructor(
    private val delegateExecute: ExecuteCommand<CMD>,
    private val reportCommandExecuted: ReportCommandExecuted<OUT>,
    private val reportError: ReportError<OUT>,
    private val outputChannel: OutputChannel<OUT>,
) : ExecuteCommand<CMD> {

    override fun invoke(rover: Rover, command: CMD): Either<MarsRoverError, Pair<Command, Rover>> =
        delegateExecute(rover, command)
            .tap { (command, updatedRover) ->
                outputChannel.display(reportCommandExecuted(command, updatedRover))
            }
            .tapLeft { error ->
                outputChannel.display(reportError(error, rover))
            }
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
