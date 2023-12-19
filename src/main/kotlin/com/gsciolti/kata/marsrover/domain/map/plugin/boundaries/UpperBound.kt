package com.gsciolti.kata.marsrover.domain.map.plugin.boundaries

import com.gsciolti.kata.marsrover.domain.model.Move

class UpperBound(private val boundary: Int) : BoundaryPlugin {

    override val isNotValid: (Move) -> Boolean
        get() = { it.nextRover.position.y >= boundary }
}