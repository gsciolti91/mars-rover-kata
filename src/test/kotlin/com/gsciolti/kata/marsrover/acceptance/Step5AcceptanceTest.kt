package com.gsciolti.kata.marsrover.acceptance

import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

@Disabled
class Step5AcceptanceTest : AcceptanceTest() {

    companion object {
        @JvmStatic
        fun testParams() = listOf(
            of(arrayOf("-start=0,2,n", "-map=4x3,w", "-command=f"), "Rover moved forward. Current [0,0:n]\n"),
            of(arrayOf("-start=3,0,e", "-map=4x3,w", "-command=f"), "Rover moved forward. Current [0,0:e]\n"),
            of(arrayOf("-start=0,0,s", "-map=4x3,w", "-command=f"), "Rover moved forward. Current [0,2:s]\n"),
            of(arrayOf("-start=0,0,w", "-map=4x3,w", "-command=f"), "Rover moved forward. Current [3,0:w]\n"),
            of(arrayOf("-start=0,0,n", "-map=4x3,w", "-command=b"), "Rover moved backward. Current [0,2:n]\n"),
            of(arrayOf("-start=0,0,e", "-map=4x3,w", "-command=b"), "Rover moved backward. Current [3,0:e]\n"),
            of(arrayOf("-start=0,2,s", "-map=4x3,w", "-command=b"), "Rover moved backward. Current [0,0:s]\n"),
            of(arrayOf("-start=3,0,w", "-map=4x3,w", "-command=b"), "Rover moved backward. Current [0,0:w]\n"),
        )
    }

    @ParameterizedTest
    @MethodSource("testParams")
    fun `should perform movements in a wrapping map`(params: Array<String>, expectedLogs: String) {

        main(*params)

        assertThat(logs()).isEqualTo(expectedLogs)
    }

    @Test
    fun `should report obstacles inside the map`() {

        main("-start=0,0,n", "-map=4x3,w", "-obstacles=[0,1]", "-command=f")

        assertThat(logs()).isEqualTo("Obstacle encountered at [0,1]. Current [0,0:n]\n")
    }

    @Test
    fun `should avoid an obstacle that is right outside the map`() {

        main("-start=0,2,n", "-map=4x3,w", "-obstacles=[0,3]", "-command=f")

        assertThat(logs()).isEqualTo("Rover moved forward. Current [0,0:n]\n")
    }
}
