package com.gsciolti.kata.marsrover.domain

sealed class Direction(val left: () -> Direction, val right: () -> Direction) {

    object North : Direction({ West }, { East })
    object East : Direction({ North }, { South })
    object South : Direction({ East }, { West })
    object West : Direction({ South }, { North })
}