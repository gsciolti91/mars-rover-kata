package com.gsciolti.kata.marsrover.domain.map.plugin.boundaries

import com.gsciolti.kata.marsrover.domain.model.Move

object LowerBound : BoundaryPlugin {

    override val isNotValid: (Move) -> Boolean
        get() = { it.nextRover.position.y < 0 }
}