package com.gsciolti.kata.marsrover.domain.map.plugin.wrap

import com.gsciolti.kata.marsrover.domain.map.Map
import com.gsciolti.kata.marsrover.domain.map.plugin.MapPlugin

class Wrap(private val width: Int, private val height: Int) : MapPlugin {

    override fun apply(): Map.Configuration.() -> Unit =
        {
            apply(
                HorizontalWrap(width),
                VerticalWrap(height)
            )
        }
}
