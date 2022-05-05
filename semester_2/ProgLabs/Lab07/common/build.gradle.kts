import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
    application
}

group = "com.joulin"
version = "3.0-SNAPSHOT"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}