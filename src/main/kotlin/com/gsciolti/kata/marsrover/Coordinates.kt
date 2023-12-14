package com.gsciolti.kata.marsrover

class Coordinates(var x: Int, var y: Int) {

    fun increaseX() = x++
    fun increaseY() = y++
    fun decreaseX() = x--
    fun decreaseY() = y--

    fun copy() = Coordinates(x, y)

    fun isOutsideOf(map: Pair<Int, Int>) =
        x < 0 || x >= map.first || y < 0 || y >= map.second
}
