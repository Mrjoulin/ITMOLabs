import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
}

group = "su.arlet"
version = "0.0.3-rolling"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-artemis")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("io.jsonwebtoken:jjwt:0.12.5")
    implementation("javax.jms:javax.jms-api:2.0.1")
    implementation("org.apache.activemq:artemis-jakarta-client:2.33.0")
    implementation("org.apache.activemq:artemis-jakarta-server:2.33.0")
    implementation("org.apache.activemq:artemis-jms-server:2.33.0")
    implementation("org.apache.activemq:artemis-jms-client:2.33.0")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation(project(":shared", "default"))
    implementation(files("libs/gozirra-client-0.4.1.jar"))
}

tasks.withType<BootBuildImage> {
    builder.set("paketobuildpacks/builder:tiny")
    environment.set(
        mapOf(
            "BP_NATIVE_IMAGE" to "true",
            "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "--no-fallback --initialize-at-build-time=org.apache.activemq.artemis.jms.client --initialize-at-build-time=org.apache.activemq.artemis.core.remoting --initialize-at-build-time=org.apache.activemq.artemis.core.server.components --initialize-at-run-time=org.apache.activemq.artemis.utils --verbose --report-unsupported-elements-at-runtime --allow-incomplete-classpath"
        )
    )
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
