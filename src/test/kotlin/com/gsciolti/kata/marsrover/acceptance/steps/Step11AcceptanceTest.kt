package com.gsciolti.kata.marsrover.acceptance.steps

import com.gsciolti.kata.marsrover.acceptance.AcceptanceTest
import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Step11AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should handle atomic commands`() {

        main(
            "-start=0,0,n",
            "-command=f_f_f"
        )

        assertThat(logs()).isEqualTo(
            """
                Rover moved forward. Current [0,1:n]
                Rover moved forward. Current [0,2:n]
                Rover moved forward. Current [0,3:n]

            """.trimIndent()
        )
    }

    @Test
    fun `should prevent an atomic command from being executed if a boundary is encountered on the path`() {

        main(
            "-start=0,0,n",
            "-map=4x4",
            "-command=f,f_f_f"
        )

        assertThat(logs()).isEqualTo(
            """
                Rover moved forward. Current [0,1:n]
                Error prevented: Boundary encountered at [0,3]. Current [0,1:n]

            """.trimIndent()
        )
    }
}
