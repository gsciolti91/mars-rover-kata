package com.gsciolti.kata.marsrover

fun main(vararg args: String) {

    val start = args.filter { it.startsWith("-start") }.first()
    val command = args.filter { it.startsWith("-command") }.first().split("=")[1]

    var x = start.split("=")[1].split(",")[0].toInt()
    var y = start.split("=")[1].split(",")[1].toInt()
    var d = start.split("=")[1].split(",")[2]

    val msg = when (command) {
        "f" -> "Rover moved forward"
        "b" -> "Rover moved backward"
        "l" -> "Rover turned left"
        "r" -> "Rover turned right"
        else -> "Invalid command '$command'"
    }

    when (Pair(command, d)) {
        "f" to "n" -> y++
        "f" to "e" -> x++
        "f" to "s" -> y--
        "f" to "w" -> x--
        "b" to "n" -> y--
        "b" to "e" -> x--
        "b" to "s" -> y++
        "b" to "w" -> x++
        "l" to "n" -> d = "w"
        "l" to "e" -> d = "n"
        "l" to "s" -> d = "e"
        "l" to "w" -> d = "s"
        "r" to "n" -> d = "e"
        "r" to "e" -> d = "s"
        "r" to "s" -> d = "w"
        "r" to "w" -> d = "n"
    }

    println("$msg. Current [$x,$y:$d]")
}