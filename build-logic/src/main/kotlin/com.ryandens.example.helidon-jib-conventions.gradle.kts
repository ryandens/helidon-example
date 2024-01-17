import com.ryandens.jlink.tasks.JlinkJreTask
import org.gradle.api.tasks.Copy

plugins {
    id("com.ryandens.example.helidon-conventions")
    id("com.ryandens.example.jlink-helidon-conventions")
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
}

jib.container {
    mainClass = "io.helidon.microprofile.cdi.Main"
}

listOf(tasks.jibDockerBuild, tasks.jibBuildTar, tasks.jib).forEach { jibTask ->
    jibTask {
        dependsOn(tasks.processJandexIndex)
        notCompatibleWithConfigurationCache("Jib is not compatible with configuration cache")
    }
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
