package com.gsciolti.kata.marsrover.domain.command.parse

import com.gsciolti.kata.marsrover.domain.command.Command
import com.gsciolti.kata.marsrover.functional.Either

interface ParseCommand<IN> : (IN) -> Either<CommandNotValid, Command>