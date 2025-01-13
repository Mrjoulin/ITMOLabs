// import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
    id("org.springframework.boot") version "3.0.13"
    id("io.spring.dependency-management") version "1.1.4"
    // id("com.github.gradlecommunity.jaxb2") version "3.1.0"
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
    implementation("org.springframework.ws:spring-ws-core:4.0.1")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery:4.0.0")
    // implementation("org.springframework.cloud:spring-cloud-starter-eureka:1.4.0.RELEASE")
    // implementation("org.springframework.cloud:spring-cloud-starter-netflix-ribbon:2.2.0.RELEASE")
    // implementation("org.springframework.cloud:spring-cloud-starter-netflix-zuul:2.2.0.RELEASE")
    //implementation("org.springframework.cloud:spring-cloud-starter-config:4.0.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.3")
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
    // implementation("org.glassfish.jersey.core:jersey-common:3.1.0")
    // implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")
    implementation("wsdl4j:wsdl4j:1.6.3")
    // implementation("org.apache.cxf:cxf-spring-boot-starter-jaxws:4.0.3")
    runtimeOnly("org.postgresql:postgresql:42.7.2")
//    implementation("com.sun.xml.bind:jaxb-core:2.3.0")
//    implementation("com.sun.xml.bind:jaxb-xjc:2.3.0")
//    implementation("com.sun.xml.bind:jaxb-impl:2.3.0")
//    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
//    implementation("javax.activation:activation:1.1.1")
//    jaxb2("org.jvnet.jaxb2_commons:jaxb2-basics-runtime:0.9.5")
//    jaxb2("org.jvnet.jaxb2_commons:jaxb2-basics-ant:0.9.5")
//    jaxb2("org.jvnet.jaxb2_commons:jaxb2-basics:0.9.5")
//    jaxb2("com.sun.xml.bind:jaxb-core:2.3.0")
//    jaxb2("com.sun.xml.bind:jaxb-xjc:2.3.0")
//    jaxb2("com.sun.xml.bind:jaxb-impl:2.3.0")
//    jaxb2("org.projectlombok:lombok:1.18.34")
//    jaxb2("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
//    jaxb2("org.glassfish.jaxb:jaxb-runtime:2.3.2")
//    jaxb2("javax.activation:activation:1.1.1")
//    jaxb2("org.slf4j:slf4j-simple:2.0.0")
    // jaxb2("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
}

//jaxb2 {
//    xjc {
//        create("generate") {
//            // generatedSourcesDir = "src/main/generated/java"
//            basePackage = "com.joulin.killerservice.entities"
//            schema = "src/main/resources/schema.xsd"
//            encoding = "UTF-8"
//        }
//    }
//}
//
//sourceSets {
//    main {
//        java {
//            srcDir("src/generated/java")
//        }
//    }
//}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

