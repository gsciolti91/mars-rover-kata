package com.gsciolti.kata.marsrover.acceptance

import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class Step12AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should prevent an atomic command from being executed if an obstacle is encountered on the path`() {

        main(
            "-start=2,2,n",
            "-command=f,b_b_b",
            "-obstacles=[2,0]"
        )

        assertThat(logs()).isEqualTo(
            """
                Rover moved forward. Current [2,3:n]
                Error prevented: Obstacle encountered at [2,0]. Current [2,3:n]

            """.trimIndent()
        )
    }
}
