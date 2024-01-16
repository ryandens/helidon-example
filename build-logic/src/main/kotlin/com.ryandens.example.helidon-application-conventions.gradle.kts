import com.ryandens.jlink.tasks.JlinkJreTask

plugins {
    id("com.ryandens.example.java-application-conventions")
    id("com.ryandens.example.weld-conventions")
    id("com.github.vlsi.jandex")
    id("com.ryandens.jlink-application")
    id("com.google.cloud.tools.jib")
    id("com.ryandens.jlink-jib")
    id("com.ryandens.temurin-binaries-repository")
}

val jdk by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
    isVisible = false
}

val copyJdks =
    tasks.register<Copy>("copyJdks") {
        from(provider { tarTree(jdk.singleFile) })
        into(project.layout.buildDirectory.dir("jdks"))
    }

dependencies {
    jdk("temurin21-binaries:OpenJDK21U-jdk_aarch64_linux_hotspot_21.0.1_12:jdk-21.0.1+12@tar.gz")
    implementation(enforcedPlatform("io.helidon:helidon-dependencies:4.0.2"))
    implementation("io.helidon.microprofile.bundles:helidon-microprofile")
    implementation("org.glassfish.jersey.media:jersey-media-json-binding")
    runtimeOnly("io.smallrye:jandex")
    runtimeOnly("jakarta.activation:jakarta.activation-api")
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-junit5")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

jlinkJre {
    modules =
        listOf(
            "java.desktop",
            "java.logging",
            "java.management",
            "java.naming",
            "java.sql",
            "java.xml",
            "jdk.unsupported",
        )
}

application {
    mainClass.set("io.helidon.microprofile.cdi.Main")
}

jib.container {
    mainClass = "io.helidon.microprofile.cdi.Main"
}

tasks.jibDockerBuild {
    notCompatibleWithConfigurationCache("Jib is not compatible with configuration cache")
}

jib.from {
    image = "gcr.io/distroless/java-base-debian11:nonroot-arm64@sha256:f4ca7f7f88b9e10329ce0d798a7035ef2e9cb6ff447766281036ce43876489aa"
}

val linuxJlinkJre =
    project.tasks.register<JlinkJreTask>("linuxJlinkJre") {
        // by default, JlinkJreTask uses the module path associated with the configured java toolchain that is executing
        // the jlink command, but this can be overridden to instead point at a different jmods directory for the purpose
        // of building a JRE for a different platform
        this.modulePath.fileProvider(copyJdks.map { File(it.destinationDir, "jdk-21.0.1+12/jmods/") })
        outputDirectory.set(file(layout.buildDirectory.dir("jlink-jre-linux")))
    }

jlinkJib {
    // by default, this plugin will include the JRE built by the jlinkJre task. Optionally, any directory can be used.
    // this enables the default jlinkJre task to build a JRE for one platform and a custom JlinkJreTask to build a JRE
    // for another platform
    jlinkJre.value(linuxJlinkJre.map { it.outputDirectory.get() })
}
