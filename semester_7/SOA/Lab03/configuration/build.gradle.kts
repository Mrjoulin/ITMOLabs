import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
    id("org.springframework.boot") version "3.0.13"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
}

group = "com.joulin"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    jvmToolchain(17)
}
dependencies {
    testImplementation(kotlin("test"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-config-server:4.0.0")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery:4.0.0")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
