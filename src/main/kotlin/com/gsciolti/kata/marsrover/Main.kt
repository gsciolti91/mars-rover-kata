package com.gsciolti.kata.marsrover

fun main(vararg args: String) {

    val start = args.filter { it.startsWith("-start") }.first()
    val command = args.filter { it.startsWith("-command") }.first().split("=")[1]
    val commands = command.split(",")
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

    val x = start.split("=")[1].split(",")[0].toInt()
    val y = start.split("=")[1].split(",")[1].toInt()
    val d = start.split("=")[1].split(",")[2]

    var newX = x
    var newY = y
    var newD = d

    for (cmd in commands) {
        val dirmsg = when (cmd) {
            "f" -> "Rover moved forward"
            "b" -> "Rover moved backward"
            "l" -> "Rover turned left"
            "r" -> "Rover turned right"
            else -> {
                println("Invalid command '$cmd'. Current [$newX,$newY:$newD]")
                break
            }
        }

        when (Pair(cmd, newD)) {
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

        if (map != null && mapType == "w") {
            if (newX == map.first) newX = 0
            if (newX == -1) newX = map.first - 1
            if (newY == map.second) newY = 0
            if (newY == -1) newY = map.second - 1
        }

        val msg: String

        if (obstacles != null && obstacles.contains(newX to newY)) {
            msg = "Obstacle encountered at [$newX,$newY]. Current [$x,$y:$d]"
        } else if (map != null && (newX < 0 || newX >= map.first || newY < 0 || newY >= map.second)) {
            msg = "Boundary encountered at [$x,$y]. Current [$x,$y:$d]"
        } else {
            msg = "$dirmsg. Current [$newX,$newY:$newD]"
        }

        println(msg)
    }
}