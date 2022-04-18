import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.72"

plugins {
    `maven-publish`
    application
    id("org.beryx.runtime") version "1.12.4"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    // id("com.palantir.graal") version "0.7.1"
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
    maven("https://oss.jfrog.org/oss-snapshot-local")
}

val ktor_version = "1.3.2"

// TODO we can compress by a ton by downloading a subset of zstd-jni (now support ALL platforms!)
dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    // implementation("com.github.zxing.zxing:core:zxing-3.4.0")
    // implementation("com.github.zxing.zxing:javase:zxing-3.4.0")
    implementation("org.apache.commons:commons-compress:1.20")
    // e.g. zstd-jni:linux_amd64:1.4.5-6
    implementation("com.github.luben:zstd-jni:1.4.5-6")
    implementation("com.github.ajalt:clikt:2.8.0")

    // Rsocket
    implementation("io.rsocket.kotlin:rsocket-core:0.10.0-SNAPSHOT")
    implementation("io.rsocket.kotlin:rsocket-transport-ktor:0.15.4")
    implementation("io.rsocket.kotlin:rsocket-transport-ktor-client:0.10.0-SNAPSHOT")
    implementation("io.rsocket.kotlin:rsocket-transport-ktor-server:0.10.0-SNAPSHOT")
    implementation("io.ktor:ktor-client-cio:1.4.1")
    implementation("io.ktor:ktor-server-cio:1.4.1")

    // KTOR
    //implementation("io.ktor:ktor-server-core:$ktor_version")
    //implementation("io.ktor:ktor-server-netty:$ktor_version")
//
    //implementation("io.ktor:ktor-client-core:$ktor_version")
    //implementation("io.ktor:ktor-client-cio:$ktor_version")
    //implementation("io.ktor:ktor-client-websockets:$ktor_version")

    //implementation("com.github.kittinunf.fuel:fuel:2.2.0")
    //implementation("com.github.kittinunf.fuel:fuel-coroutines:2.2.0")
    /**
    Compression:
    implementation("org.xerial.snappy:snappy-java:1.1.7.6")

    Serialization:
    https://github.com/Kotlin/kotlinx.serialization

    HTTP w/o reflections:
    https://fuel.gitbook.io/documentation/
    https://dev.to/viniciusccarvalho/building-a-native-cli-with-kotlin-and-graalvm-55ee
     */

    // Test Libs
    testImplementation("org.amshove.kluent:kluent:1.59")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    implementation("org.slf4j:slf4j-simple:1.7.26")
}

// Using graal achieves 50% size reduction of the run-file.
// graal {
//     mainClass("com.londogard.synk.SynkKt")
//     outputName("synk")
//     // option("-H:+ReportExceptionStackTraces")
//     // option("--no-fallback")
//     // option("--static")
// }

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnit()
}

runtime {
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    jpackage {
        installerType = "deb" // https://badass-runtime-plugin.beryx.org/releases/latest/
        // TODO better if modular (smaller!)
    }
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
