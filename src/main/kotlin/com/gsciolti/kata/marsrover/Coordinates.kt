package com.gsciolti.kata.marsrover

class Coordinates(var x: Int, var y: Int) {

    fun increaseX() = Coordinates(x + 1, y)
    fun increaseY() = Coordinates(x, y + 1)
    fun decreaseX() = Coordinates(x - 1, y)
    fun decreaseY() = Coordinates(x, y - 1)

    fun copy() = Coordinates(x, y)
}
