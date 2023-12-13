package com.gsciolti.kata.marsrover.acceptance

import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class Step6AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should handle multiple commands in a row`() {

        main("-start=0,0,n", "-command=f,f,b,l,b,r,f")

        assertThat(logs()).isEqualTo(
            """
                Rover moved forward. Current [0,1:n]
                Rover moved forward. Current [0,2:n]
                Rover moved backward. Current [0,1:n]
                Rover turned left. Current [0,1:w]
                Rover moved backward. Current [1,1:w]
                Rover turned right. Current [1,1:n]
                Rover moved forward. Current [1,2:n]
            
            """.trimIndent()
        )
    }
}
