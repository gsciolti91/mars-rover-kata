package com.gsciolti.kata.marsrover.domain.report.output

class MultipleOutputChannel<T> private constructor(
    private val channels: MutableList<OutputChannel<T>>
) : OutputChannel<T> {

    constructor(vararg channels: OutputChannel<T>) : this(channels.toMutableList())

    override fun flush() =
        channels.forEach { it.flush() }

    override fun display(value: Output.Value<T>) =
        channels.forEach { it.display(value) }

    override fun plus(other: OutputChannel<T>): OutputChannel<T> =
        also { channels.add(other) }
}
