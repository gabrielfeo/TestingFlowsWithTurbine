plugins {
    kotlin("jvm") version "1.4.32"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-common:1.4.32")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.4.32")
    testImplementation("app.cash.turbine:turbine:0.4.1")
}

tasks {
    test {
        useJUnitPlatform()
        maxParallelForks = 8
    }
}
