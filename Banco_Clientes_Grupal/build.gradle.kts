plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
    id("org.jetbrains.dokka") version "1.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Logger
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.slf4j:slf4j-simple:1.7.32")

    // Lombok
    implementation("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")

    // Either
    implementation("io.vavr:vavr:0.10.4")

    // Hikari
    implementation("com.zaxxer:HikariCP:5.0.1")

    // ScriptRunner
    implementation("org.mybatis:mybatis:3.5.13")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.18.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")

    // PostgresSQL
    implementation("org.postgresql:postgresql:42.7.4")

    // SqLite
    implementation("org.xerial:sqlite-jdbc:3.45.2.0")

    // Mordant
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta9")

    // RXJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.9")

    // Reactor Core
    implementation("io.projectreactor:reactor-core:3.5.7")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:1.20.2")
    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
    testImplementation("org.testcontainers:postgresql:1.20.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))

    // Mockito
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("io.projectreactor:reactor-test:3.6.7")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:1.20.2")
    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
    testImplementation("org.testcontainers:postgresql:1.20.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.11.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveFileName.set("banco_clientes.jar")
    manifest {
        attributes["Main-Class"] = "org.example.Main"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}