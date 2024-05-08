plugins {

    kotlin("jvm") version "1.9.23"

}


group = "ptp.fltv"
version = "1.0-SNAPSHOT"


repositories {

    mavenCentral()

}


dependencies {

    testImplementation(kotlin("test"))
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")

}


tasks.test {

    useJUnitPlatform()

}


kotlin {

    jvmToolchain(17)

}