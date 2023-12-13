package com.gsciolti.kata.marsrover.acceptance

import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

class Step1AcceptanceTest : AcceptanceTest() {

    companion object {
        @JvmStatic
        fun testParams() = listOf(
            of(arrayOf("-start=0,0,n", "-command=f"), "Rover moved forward. Current [0,1:n]\n"),
            of(arrayOf("-start=0,0,e", "-command=f"), "Rover moved forward. Current [1,0:e]\n"),
            of(arrayOf("-start=0,0,s", "-command=f"), "Rover moved forward. Current [0,-1:s]\n"),
            of(arrayOf("-start=0,0,w", "-command=f"), "Rover moved forward. Current [-1,0:w]\n"),

            of(arrayOf("-start=0,0,n", "-command=b"), "Rover moved backward. Current [0,-1:n]\n"),
            of(arrayOf("-start=0,0,e", "-command=b"), "Rover moved backward. Current [-1,0:e]\n"),
            of(arrayOf("-start=0,0,s", "-command=b"), "Rover moved backward. Current [0,1:s]\n"),
            of(arrayOf("-start=0,0,w", "-command=b"), "Rover moved backward. Current [1,0:w]\n"),

            of(arrayOf("-start=0,0,n", "-command=l"), "Rover turned left. Current [0,0:w]\n"),
            of(arrayOf("-start=0,0,e", "-command=l"), "Rover turned left. Current [0,0:n]\n"),
            of(arrayOf("-start=0,0,s", "-command=l"), "Rover turned left. Current [0,0:e]\n"),
            of(arrayOf("-start=0,0,w", "-command=l"), "Rover turned left. Current [0,0:s]\n"),

            of(arrayOf("-start=0,0,n", "-command=r"), "Rover turned right. Current [0,0:e]\n"),
            of(arrayOf("-start=0,0,e", "-command=r"), "Rover turned right. Current [0,0:s]\n"),
            of(arrayOf("-start=0,0,s", "-command=r"), "Rover turned right. Current [0,0:w]\n"),
            of(arrayOf("-start=0,0,w", "-command=r"), "Rover turned right. Current [0,0:n]\n")
        )
    }

    @ParameterizedTest
    @MethodSource("testParams")
    fun `should perform basic movements`(params: Array<String>, expectedLogs: String) {

        main(*params)

        assertThat(logs()).isEqualTo(expectedLogs)
    }
}
