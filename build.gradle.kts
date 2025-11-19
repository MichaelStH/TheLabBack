plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.ksp)

    application
}

group = "com.riders.thelabback"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)

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

    // Koin
    implementation(platform(libs.koin.bom))
    ksp(libs.koin.ksp.compiler)
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger)
    implementation(platform(libs.koin.annotations.bom))
    implementation(libs.koin.annotations)

    // Kotlin
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Ktor
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.http)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.serialization.jvm)
    implementation(libs.ktor.serialization.xml)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.content.negotiation)
//    implementation(libs.ktor.server.di)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.utils)

    implementation(libs.logback)

    // Kotools Types : incompatible with Serialization 1.9.0
    // implementation(libs.types)

    // Arbor Logging
    implementation(libs.arbor)

    ///////////////////////////////////////////////////////
    //
    // TESTS
    //
    ///////////////////////////////////////////////////////
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.test.junit)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)

    testImplementation(libs.ktor.server.test.host)
}

tasks.test {
    useJUnitPlatform()
}

// Heroku
tasks.create("stage") {
    dependsOn("installDist")
}