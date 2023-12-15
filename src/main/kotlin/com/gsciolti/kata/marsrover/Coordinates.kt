package com.gsciolti.kata.marsrover

data class Coordinates(val x: Int, val y: Int) {

    fun increaseX() = Coordinates(x + 1, y)
    fun increaseY() = Coordinates(x, y + 1)
    fun decreaseX() = Coordinates(x - 1, y)
    fun decreaseY() = Coordinates(x, y - 1)
}
