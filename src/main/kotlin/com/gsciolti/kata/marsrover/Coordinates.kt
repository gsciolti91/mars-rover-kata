package com.gsciolti.kata.marsrover

data class Coordinates(var x: Int, var y: Int) {

    fun increaseX() = x++
    fun increaseY() = y++
    fun decreaseX() = x--
    fun decreaseY() = y--

    fun copy() = Coordinates(x, y)
}
