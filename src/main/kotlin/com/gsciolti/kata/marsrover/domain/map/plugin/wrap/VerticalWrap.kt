package com.gsciolti.kata.marsrover.domain.map.plugin.wrap

import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.map.plugin.MapPlugin
import com.gsciolti.kata.marsrover.domain.model.Coordinates
import com.gsciolti.kata.marsrover.domain.model.Move

class VerticalWrap(private val height: Int) : MapPlugin {

    override fun apply(): Map.Configuration.() -> Unit = {
        moveAdjustments.add { move: Move ->
            move.copy(nextRover = move.nextRover.copy(position = adjust(move.nextRover.position)))
        }
    }

    private fun adjust(coordinates: Coordinates) =
        when (coordinates.y) {
            height -> coordinates.copy(y = 0)
            -1 -> coordinates.copy(y = height - 1)
            else -> coordinates
        }
}