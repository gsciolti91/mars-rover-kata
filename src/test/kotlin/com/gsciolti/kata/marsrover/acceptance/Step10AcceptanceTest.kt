package com.gsciolti.kata.marsrover.acceptance

import com.gsciolti.kata.marsrover.main
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

@Disabled
class Step10AcceptanceTest : AcceptanceTest() {

    @Test
    fun `should write the output to a file as well as to the standard output`() {

        main("-start=0,0,n", "-command=f,f,b,l,b,r,f", "-out=${file.name}")

        val output = """
                Rover moved forward. Current [0,1:n]
                Rover moved forward. Current [0,2:n]
                Rover moved backward. Current [0,1:n]
                Rover turned left. Current [0,1:w]
                Rover moved backward. Current [1,1:w]
                Rover turned right. Current [1,1:n]
                Rover moved forward. Current [1,2:n]

            """.trimIndent()

        assertThat(logs()).isEqualTo(output)
        assertThat(contentOf(file)).isEqualTo(output)

        file.delete()
    }

    private val file = File("output.txt")

    private fun contentOf(file: File): String =
        file.readText()
}
