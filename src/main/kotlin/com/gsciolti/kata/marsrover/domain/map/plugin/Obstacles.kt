package com.gsciolti.kata.marsrover.domain.map.plugin

import com.gsciolti.kata.marsrover.domain.command.execute.error.ObstacleEncountered
import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.model.Coordinates
import com.gsciolti.kata.marsrover.functional.left
import com.gsciolti.kata.marsrover.functional.right

class Obstacles(private val coordinates: List<Coordinates>) : MapPlugin {

    override fun apply(): Map.Configuration.() -> Unit = {
        moveValidations.add { move ->
            if (coordinates.contains(move.nextRover.position))
                ObstacleEncountered(move, move.currentRover).left()
            else
                move.right()
        }
    }
}