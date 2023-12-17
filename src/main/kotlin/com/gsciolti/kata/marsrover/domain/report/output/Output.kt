package com.gsciolti.kata.marsrover.domain.report.output

abstract class Output<T>(val value: T) {

    abstract operator fun plus(other: Output<T>): Output<T>
}