import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.spring") version "1.5.20"
}

group = "com.joulin"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_13
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery:2.2.8.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-ribbon:2.2.0.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-zuul:2.2.0.RELEASE")
    implementation("ch.qos.logback:logback-classic")
    implementation("ch.qos.logback:logback-core")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "13"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
