package com.gsciolti.kata.marsrover.acceptance.steps

import com.gsciolti.kata.marsrover.acceptance.AcceptanceTest
import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Step9AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should execute commands until a boundary is encountered`() {

        main(
            "-start=0,0,e",
            "-map=3x2",
            "-command=f,f,f,b"
        )

        assertThat(logs()).isEqualTo(
            """
                Rover moved forward. Current [1,0:e]
                Rover moved forward. Current [2,0:e]
                Boundary encountered at [2,0]. Current [2,0:e]

            """.trimIndent()
        )
    }
}
