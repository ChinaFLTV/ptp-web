plugins {

    kotlin("jvm") version "1.9.23"

}


group = "ptp.fltv"
version = "1.0-SNAPSHOT"


repositories {

    mavenCentral()
    maven { setUrl("https://repo.spring.io/milestone") }
    maven { setUrl("https://repo.spring.io/snapshot") }

}


dependencies {

    testImplementation(kotlin("test"))
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.2.4")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.1.2")
    testImplementation("junit:junit:4.13.1")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.2.4")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config:2023.0.0.0-RC1")
    implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery:2023.0.0.0-RC1")
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.5.0")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("cn.hutool:hutool-all:5.8.27")

}


tasks.test {

    useJUnitPlatform()

}


kotlin {

    jvmToolchain(17)

}