import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    kotlin("plugin.jpa") version "2.0.20"
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
    implementation("org.springframework:spring-webmvc:6.1.13")
    implementation("org.springframework:spring-context:6.1.13")
    implementation("org.springframework:spring-orm:6.1.13")
    implementation("org.springframework.data:spring-data-commons:3.3.4")
    implementation("org.springframework.data:spring-data-jpa:3.3.4")
    implementation("org.springframework.data:spring-data-rest-webmvc:4.3.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.6")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("org.hibernate.orm:hibernate-core:6.5.3.Final")
    implementation("ch.qos.logback:logback-classic:1.5.8")
    implementation("org.projectlombok:lombok:1.18.34")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    runtimeOnly("org.postgresql:postgresql:42.7.2")
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
