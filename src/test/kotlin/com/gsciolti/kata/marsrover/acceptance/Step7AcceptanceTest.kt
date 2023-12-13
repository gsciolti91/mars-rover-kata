package com.gsciolti.kata.marsrover.acceptance

import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class Step7AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should execute commands until the first not valid one is encountered`() {

        main("-start=0,0,n", "-command=f,f,x,f")

        assertThat(logs()).isEqualTo(
            """
                Rover moved forward. Current [0,1:n]
                Rover moved forward. Current [0,2:n]
                Invalid command 'x'. Current [0,2:n]

            """.trimIndent()
        )
    }
}
