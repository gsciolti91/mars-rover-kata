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

    val startX = start.split("=")[1].split(",")[0].toInt()
    val startY = start.split("=")[1].split(",")[1].toInt()
    val startD = start.split("=")[1].split(",")[2]

    var currentX = startX
    var currentY = startY
    var currentD = startD

    var newX = currentX
    var newY = currentY
    var newD = currentD

    for (cmd in commands) {
        val dirmsg = when (cmd) {
            "f" -> "Rover moved forward"
            "b" -> "Rover moved backward"
            "l" -> "Rover turned left"
            "r" -> "Rover turned right"
            else -> {
                println("Invalid command '$cmd'. Current [$currentX,$currentY:$currentD]")
                break
            }
        }

        when (Pair(cmd, currentD)) {
            "f" to "n" -> newY = currentY + 1
            "f" to "e" -> newX = currentX + 1
            "f" to "s" -> newY = currentY - 1
            "f" to "w" -> newX = currentX - 1
            "b" to "n" -> newY = currentY - 1
            "b" to "e" -> newX = currentX - 1
            "b" to "s" -> newY = currentY + 1
            "b" to "w" -> newX = currentX + 1
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
            println("Obstacle encountered at [$newX,$newY]. Current [$currentX,$currentY:$currentD]")
            break
        } else if (map != null && (newX < 0 || newX >= map.first || newY < 0 || newY >= map.second)) {
            msg = "Boundary encountered at [$startX,$startY]. Current [$startX,$startY:$startD]"
        } else {
            msg = "$dirmsg. Current [$newX,$newY:$newD]"

            currentX = newX
            currentY = newY
            currentD = newD
        }

        println(msg)
    }
}