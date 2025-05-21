plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.polina.practice"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation ("org.springframework.kafka:spring-kafka")
    implementation ("org.apache.kafka:kafka-clients")
    implementation ("org.polina.practice:common-model:1.0-SNAPSHOT")
    implementation ("org.polina.practice:common-config:1.0-SNAPSHOT")
}
subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        implementation("org.springframework.kafka:spring-kafka")
        implementation("org.apache.kafka:kafka-clients")
        implementation ("org.polina.practice:common-model:1.0-SNAPSHOT")
        implementation ("org.polina.practice:common-config:1.0-SNAPSHOT")
    }
}
tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = false
    }
}

tasks.test {
    useJUnitPlatform()
}