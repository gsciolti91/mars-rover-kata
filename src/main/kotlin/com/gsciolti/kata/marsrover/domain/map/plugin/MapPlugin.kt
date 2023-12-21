package com.gsciolti.kata.marsrover.domain.map.plugin

import com.gsciolti.kata.marsrover.domain.map.Map

interface MapPlugin {
    fun apply(): Map.Configuration.() -> Unit
}