import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.gsciolti"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // todo jack arrow

    val junit5 = "5.10.1"
    val assertJ = "3.24.2"

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.assertj:assertj-core:$assertJ")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events = setOf(FAILED, PASSED, SKIPPED)
        showStandardStreams = true
        exceptionFormat = FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

kotlin {
    jvmToolchain(21)
}