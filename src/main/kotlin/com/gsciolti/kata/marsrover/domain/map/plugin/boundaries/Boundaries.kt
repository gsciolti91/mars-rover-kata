package com.gsciolti.kata.marsrover.domain.map.plugin.boundaries

import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.map.plugin.MapPlugin

class Boundaries(private val width: Int, private val height: Int) : MapPlugin {

    override fun apply(): Map.Configuration.() -> Unit =
        {
            apply(
                LeftBound,
                LowerBound,
                RightBound(width),
                UpperBound(height)
            )
        }
}
