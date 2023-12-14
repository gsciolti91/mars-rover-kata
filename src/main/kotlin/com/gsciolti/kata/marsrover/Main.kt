package com.gsciolti.kata.marsrover

fun main(vararg args: String) {

    val start = args.filter { it.startsWith("-start") }.first()
    val command = args.filter { it.startsWith("-command") }.first().split("=")[1]
    val obstacles: List<Pair<Int, Int>>? = args
        .filter { it.startsWith("-obstacles") }
        .getOrNull(0)
        ?.let {
            it.split("=")[1]
                .split(";")
                .map {
                    val rawCoordinates = it.split(",")
                    rawCoordinates[0].replace("[", "").toInt() to rawCoordinates[1].replace("]", "").toInt()
                }
        }

    val x = start.split("=")[1].split(",")[0].toInt()
    val y = start.split("=")[1].split(",")[1].toInt()
    val d = start.split("=")[1].split(",")[2]

    var newX = x
    var newY = y
    var newD = d

    var dirmsg = when (command) {
        "f" -> "Rover moved forward"
        "b" -> "Rover moved backward"
        "l" -> "Rover turned left"
        "r" -> "Rover turned right"
        else -> "Invalid command '$command'"
    }

    when (Pair(command, d)) {
        "f" to "n" -> newY++
        "f" to "e" -> newX++
        "f" to "s" -> newY--
        "f" to "w" -> newX--
        "b" to "n" -> newY--
        "b" to "e" -> newX--
        "b" to "s" -> newY++
        "b" to "w" -> newX++
        "l" to "n" -> newD = "w"
        "l" to "e" -> newD = "n"
        "l" to "s" -> newD = "e"
        "l" to "w" -> newD = "s"
        "r" to "n" -> newD = "e"
        "r" to "e" -> newD = "s"
        "r" to "s" -> newD = "w"
        "r" to "w" -> newD = "n"
    }

    val msg: String

    if (obstacles != null && obstacles.contains(newX to newY)) {
        msg = "Obstacle encountered at [$newX,$newY]. Current [$x,$y:$d]"
    } else {
        msg = "$dirmsg. Current [$newX,$newY:$newD]"
    }

    println(msg)
}