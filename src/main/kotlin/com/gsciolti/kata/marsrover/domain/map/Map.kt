package com.gsciolti.kata.marsrover.domain.map

import com.gsciolti.kata.marsrover.domain.command.execute.error.ExecuteCommandError
import com.gsciolti.kata.marsrover.domain.map.plugin.MapPlugin
import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.domain.model.Rover
import com.gsciolti.kata.marsrover.functional.Either
import com.gsciolti.kata.marsrover.functional.check

class Map private constructor(
    private val moveAdjustments: Collection<(Move) -> Move>,
    private val moveValidations: Collection<(Move) -> Either<ExecuteCommandError, Move>>
) {
    companion object {
        operator fun invoke(configure: Configuration.() -> Unit): Map =
            Configuration()
                .also(configure)
                .build()
    }

    fun validate(move: Move): Either<ExecuteCommandError, Rover> =
        moveAdjustments
            .fold(move) { updatedMove, nextAdjust -> nextAdjust(updatedMove) }
            .check(moveValidations)
            .map { it.nextRover }

    class Configuration(
        val moveAdjustments: MutableList<(Move) -> Move> = mutableListOf(),
        val moveValidations: MutableList<(Move) -> Either<ExecuteCommandError, Move>> = mutableListOf()
    ) {
        fun apply(plugin: MapPlugin) {
            also(plugin.apply())
        }

        fun apply(vararg plugins: MapPlugin) =
            plugins.forEach { apply(it) }

        fun build() = Map(moveAdjustments, moveValidations)
    }
}
