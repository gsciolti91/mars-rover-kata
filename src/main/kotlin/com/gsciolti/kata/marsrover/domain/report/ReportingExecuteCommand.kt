package com.gsciolti.kata.marsrover.domain.report

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.domain.command.execute.ExecuteCommand
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.domain.report.output.OutputChannel
import com.gsciolti.kata.marsrover.domain.report.output.plus
import com.gsciolti.kata.marsrover.functional.Either

class ReportingExecuteCommand<CMD, OUT> internal constructor(
    private val delegateExecute: ExecuteCommand<CMD>,
    private val reportRoverPosition: Report<Rover, OUT>,
    private val reportCommandExecuted: Report<Command, OUT>,
    private val reportError: Report<Any, OUT>,
    private val outputChannel: OutputChannel<OUT>,
) : ExecuteCommand<CMD> {

    override fun invoke(rover: Rover, command: CMD): Either<Any, Pair<Command, Rover>> =
        delegateExecute(rover, command)
            .tap { (command, updatedRover) ->
                outputChannel.display(reportCommandExecuted(command) + reportRoverPosition(updatedRover))
            }
            .tapLeft { error ->
                outputChannel.display(reportError(error) + reportRoverPosition(rover))
            }
}

fun <IN, OUT> ExecuteCommand<IN>.reportingWith(
    reportRoverPosition: Report<Rover, OUT>,
    reportCommandExecuted: Report<Command, OUT>,
    reportError: Report<Any, OUT>,
    outputChannel: OutputChannel<OUT>
) =
    ReportingExecuteCommand(
        this,
        reportRoverPosition,
        reportCommandExecuted,
        reportError,
        outputChannel
    )
