package com.gsciolti.kata.marsrover.acceptance

import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class Step3AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should handle obstacles`() {
        main(
            "-start=0,0,n",
            "-obstacles=[0,1];[100,100]",
            "-command=f"
        )

        assertThat(logs()).isEqualTo("Obstacle encountered at [0,1]. Current [0,0:n]\n")
    }
}
