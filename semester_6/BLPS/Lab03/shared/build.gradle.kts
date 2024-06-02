plugins {
    kotlin("jvm") version "1.9.22"
}

group = "su.arlet"
version = "0.0.3-rolling"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}