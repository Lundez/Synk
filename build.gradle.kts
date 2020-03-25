import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.70"

plugins {
    `maven-publish`
    kotlin("jvm") version "1.3.70"
}

group = "com.smor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    //maven { url 'https://oss.jfrog.org/oss-release-local' }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

    /**
    Compression:
    implementation 'org.apache.commons:commons-compress:1.18'
    implementation group: "com.github.luben", name: "zstd-jni", version: "1.4.3-1"
    implementation 'org.xerial.snappy:snappy-java:1.1.7.3'

    Serialization:
    https://github.com/Kotlin/kotlinx.serialization

    CLI:
    https://github.com/Kotlin/kotlinx.cli
    https://ajalt.github.io/clikt/
     */

    // Test Libs
    testImplementation("org.amshove.kluent:kluent:1.59")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("org.slf4j:slf4j-simple:1.7.26")
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
