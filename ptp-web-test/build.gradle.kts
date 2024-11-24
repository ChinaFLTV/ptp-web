@file:Suppress("VulnerableLibrariesLocal")

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
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:4.5.0")// 2024-5-10  22:08-Knife4j尚未适配SpringBoot-WebFlux，这里引入仅是为了编写注解
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("cn.hutool:hutool-all:5.8.27")

    // 2024-6-17  19:43-搭建ELK日志系统所需依赖
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    // 2024-10-25  21:48-生成批量插入Rate的SQL语句所需依赖
    testImplementation("com.alibaba.fastjson2:fastjson2:2.0.53")
    // 2024-10-25  21:53-随机生成汉字成语的库(仓库地址 : https://github.com/yindz/common-random)
    testImplementation("com.apifan.common:common-random:1.0.21")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")

}


tasks.test {

    useJUnitPlatform()

}


kotlin {

    jvmToolchain(17)

}