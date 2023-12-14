package com.gsciolti.kata.marsrover.acceptance.steps

import com.gsciolti.kata.marsrover.acceptance.AcceptanceTest
import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

class Step2AcceptanceTest : AcceptanceTest() {

    companion object {
        @JvmStatic
        fun testParams() = listOf(
            of(arrayOf("-start=0,0,n", "-command=z"), "Invalid command 'z'. Current [0,0:n]\n"),
            of(arrayOf("-start=0,0,n", "-command=w"), "Invalid command 'w'. Current [0,0:n]\n")
        )
    }

    @ParameterizedTest
    @MethodSource("testParams")
    fun `should report error if the command is not recognized`(params: Array<String>, expectedLogs: String) {

        main(*params)

        assertThat(logs()).isEqualTo(expectedLogs)
    }
}
