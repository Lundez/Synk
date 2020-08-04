import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.72"

plugins {
    `maven-publish`
    application
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("com.palantir.graal") version "0.7.1"
    kotlin("jvm") version "1.3.72"
}

group = "com.londogard"
version = "1.0-SNAPSHOT"
application {
    mainClassName = "com.londogard.synk.SynkKt"
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
    //maven { url 'https://oss.jfrog.org/oss-release-local' }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("com.github.zxing.zxing:core:zxing-3.4.0")
    implementation("com.github.zxing.zxing:javase:zxing-3.4.0")
    implementation("org.apache.commons:commons-compress:1.20")
    implementation("com.github.luben:zstd-jni:1.4.5-6")
    implementation("com.github.ajalt:clikt:2.8.0")
    /**
    Compression:
    implementation("org.xerial.snappy:snappy-java:1.1.7.6")
    implementation group: "com.github.luben", name: "zstd-jni", version: "1.4.3-1"


    Serialization:
    https://github.com/Kotlin/kotlinx.serialization

    CLI:
    https://github.com/Kotlin/kotlinx.cli
    https://ajalt.github.io/clikt/
    https://picocli.info/
    https://fuel.gitbook.io/documentation/
    https://dev.to/viniciusccarvalho/building-a-native-cli-with-kotlin-and-graalvm-55ee
     */

    // Test Libs
    testImplementation("org.amshove.kluent:kluent:1.59")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.slf4j:slf4j-simple:1.7.26")
}

graal {
    graalVersion("20.1.0.r8-grl")
    mainClass("com.londogard.synk.SynkKt")
    outputName("synk")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnit()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/lundez/synk")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
