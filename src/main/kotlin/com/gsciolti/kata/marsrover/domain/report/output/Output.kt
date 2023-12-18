package com.gsciolti.kata.marsrover.domain.report.output

import com.gsciolti.kata.marsrover.domain.report.output.Output.None
import com.gsciolti.kata.marsrover.domain.report.output.Output.Value

sealed interface Output<out T> {

    abstract class Value<T>(val value: T) : Output<T> {
        abstract operator fun plus(other: Value<T>): Value<T>
    }

    object None : Output<Nothing>
}

infix operator fun <T> Output<T>.plus(other: Output<T>): Output<T> =
    when (this) {
        is Value<T> -> when (other) {
            is Value<T> -> this + other
            is None -> None
        }
        is None -> other
    }
