package com.gsciolti.kata.marsrover.acceptance.steps

import com.gsciolti.kata.marsrover.acceptance.AcceptanceTest
import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class Step8AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should execute commands until an obstacle is encountered`() {

        main(
            "-start=0,0,n",
            "-obstacles=[0,2]",
            "-command=f,f,r"
        )

        assertThat(logs()).isEqualTo(
            """
                Rover moved forward. Current [0,1:n]
                Obstacle encountered at [0,2]. Current [0,1:n]

            """.trimIndent()
        )
    }
}
