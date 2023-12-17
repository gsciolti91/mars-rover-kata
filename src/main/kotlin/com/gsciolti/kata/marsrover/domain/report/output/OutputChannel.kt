package com.gsciolti.kata.marsrover.domain.report.output

interface OutputChannel<T> {

    fun display(output: Output<T>)
}