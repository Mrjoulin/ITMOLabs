// import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
    id("org.springframework.boot") version "3.0.13"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.spring") version "2.0.20"
    kotlin("plugin.jpa") version "2.0.20"
    // id("com.github.johnrengelman.shadow") version "7.1.2"
}

//dependencyManagement {
//    imports {
//        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
//    }
//}

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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery:4.0.0")
    //implementation("org.springframework.cloud:spring-cloud-starter-eureka:1.4.0.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-ribbon:2.2.0.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-zuul:2.2.0.RELEASE")
    //implementation("org.springframework.cloud:spring-cloud-starter-config:4.0.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.8")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("org.hibernate.orm:hibernate-core:6.1.4.Final")
    implementation("ch.qos.logback:logback-classic")
    implementation("ch.qos.logback:logback-core")
    implementation("org.projectlombok:lombok:1.18.34")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.glassfish.jersey.core:jersey-common:3.1.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")
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
