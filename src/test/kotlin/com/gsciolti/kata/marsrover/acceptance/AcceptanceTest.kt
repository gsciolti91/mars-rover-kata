package com.gsciolti.kata.marsrover.acceptance

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream

abstract class AcceptanceTest {

    private lateinit var orig: PrintStream
    private lateinit var outputStream: OutputStream

    protected fun logs() = outputStream.toString()

    @BeforeEach
    fun setUp() {
        orig = System.out
        outputStream = ByteArrayOutputStream()

        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(orig)
    }
}
