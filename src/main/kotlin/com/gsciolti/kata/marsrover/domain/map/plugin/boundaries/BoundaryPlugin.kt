package com.gsciolti.kata.marsrover.domain.map.plugin.boundaries

import com.gsciolti.kata.marsrover.domain.command.execute.error.BoundaryEncountered
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.map.plugin.MapPlugin
import com.gsciolti.kata.marsrover.domain.model.Move
import com.gsciolti.kata.marsrover.functional.left
import com.gsciolti.kata.marsrover.functional.right

interface BoundaryPlugin : MapPlugin {

    val isNotValid: (Move) -> Boolean

    override fun apply(): Map.Configuration.() -> Unit = {
        moveValidations.add { move: Move ->
            if (isNotValid(move))
                BoundaryEncountered(move).left()
            else
                move.right()
        }
    }
}