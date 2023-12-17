package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.Rover
import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteCommand
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel
import com.gsciolti.kata.marsrover.functional.Either

class ReportingExecuteCommand<IN, OUT> internal constructor(
    private val delegateExecute: ExecuteCommand<IN>,
    private val reportRoverPosition: ReportRoverPosition<OUT>,
    private val reportCommandExecuted: ReportCommandExecuted<OUT>,
    private val reportError: ReportError<OUT>,
    private val outputChannel: OutputChannel<OUT>,
) : ExecuteCommand<IN> {

    override fun invoke(rover: Rover, command: IN): Either<Any, Pair<Command, Rover>> =
        delegateExecute(rover, command)
            .tap { (command, updatedRover) ->
                outputChannel.display(reportCommandExecuted(command) + reportRoverPosition(updatedRover))
            }
            .tapLeft { error ->
                outputChannel.display(reportError(error) + reportRoverPosition(rover))
            }
}

fun <IN, OUT> ExecuteCommand<IN>.reportingWith(
    reportRoverPosition: ReportRoverPosition<OUT>,
    reportCommandExecuted: ReportCommandExecuted<OUT>,
    reportError: ReportError<OUT>,
    outputChannel: OutputChannel<OUT>
) =
    ReportingExecuteCommand(
        this,
        reportRoverPosition,
        reportCommandExecuted,
        reportError,
        outputChannel
    )
