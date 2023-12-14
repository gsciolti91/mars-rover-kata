package com.gsciolti.kata.marsrover

fun main(vararg args: String) {

    val start = args.filter { it.startsWith("-start") }.first().split("=")[1]
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
    var mapType = ""
    val map: Pair<Int, Int>? = args
        .filter { it.startsWith("-map") }
        .getOrNull(0)
        ?.let {
            val rawMapAndType = it.split("=")[1].split(",")
            val rawMap = rawMapAndType[0].split("x")
            mapType = rawMapAndType.getOrElse(1) { "" }

            rawMap[0].toInt() to rawMap[1].toInt()
        }

    val currentPosition = Coordinates(start.split(",")[0].toInt(), start.split(",")[1].toInt())
    var currentD = start.split(",")[2]

    var newPosition = currentPosition.copy()
    var newD = currentD

    val commands = command.split(",")

    for (cmd in commands) {
        val dirmsg = when (cmd) {
            "f" -> "Rover moved forward"
            "b" -> "Rover moved backward"
            "l" -> "Rover turned left"
            "r" -> "Rover turned right"
            else -> {
                println("Invalid command '$cmd'. Current [${currentPosition.x},${currentPosition.y}:$currentD]")
                break
            }
        }

        when (Pair(cmd, currentD)) {
            "f" to "n" -> newPosition = currentPosition.increaseY()
            "f" to "e" -> newPosition = currentPosition.increaseX()
            "f" to "s" -> newPosition = currentPosition.decreaseY()
            "f" to "w" -> newPosition = currentPosition.decreaseX()
            "b" to "n" -> newPosition = currentPosition.decreaseY()
            "b" to "e" -> newPosition = currentPosition.decreaseX()
            "b" to "s" -> newPosition = currentPosition.increaseY()
            "b" to "w" -> newPosition = currentPosition.increaseX()
            "l" to "n" -> newD = "w"
            "l" to "e" -> newD = "n"
            "l" to "s" -> newD = "e"
            "l" to "w" -> newD = "s"
            "r" to "n" -> newD = "e"
            "r" to "e" -> newD = "s"
            "r" to "s" -> newD = "w"
            "r" to "w" -> newD = "n"
        }

        if (map != null && mapType == "w") {
            if (newPosition.x == map.first) newPosition.x = 0
            if (newPosition.x == -1) newPosition.x = map.first - 1
            if (newPosition.y == map.second) newPosition.y = 0
            if (newPosition.y == -1) newPosition.y = map.second - 1
        }

        val msg: String

        if (obstacles != null && obstacles.contains(newPosition.x to newPosition.y)) {
            println("Obstacle encountered at [${newPosition.x},${newPosition.y}]. Current [${currentPosition.x},${currentPosition.y}:$currentD]")
            break
        } else if (map != null && (newPosition.x < 0 || newPosition.x >= map.first || newPosition.y < 0 || newPosition.y >= map.second)) {
            println("Boundary encountered at [${currentPosition.x},${currentPosition.y}]. Current [${currentPosition.x},${currentPosition.y}:$currentD]")
            break
        } else {
            msg = "$dirmsg. Current [${newPosition.x},${newPosition.y}:$newD]"

            currentPosition.x = newPosition.x
            currentPosition.y = newPosition.y
            currentD = newD
        }

        println(msg)
    }
}