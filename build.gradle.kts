import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)

    application
}

group = "com.riders.thelabback"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

application {
    mainClass.set("com.riders.thelabback.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    ///////////////////////////////////////////////////////
    //
    // GENERAL DEPENDENCIES
    //
    ///////////////////////////////////////////////////////
    // Exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    runtimeOnly(libs.postgresql)
    // H2 Database
    implementation(libs.h2)

    // Kotlin standard lib
    implementation(kotlin("stdlib"))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    // Ktor
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.serialization.xml)

    implementation(libs.logback)

    ///////////////////////////////////////////////////////
    //
    // TESTS
    //
    ///////////////////////////////////////////////////////
    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}

// Heroku
tasks.create("stage") {
    dependsOn("installDist")
}