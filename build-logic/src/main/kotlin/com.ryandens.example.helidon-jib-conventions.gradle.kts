import com.ryandens.jlink.tasks.JlinkJreTask
import org.gradle.api.tasks.Copy

plugins {
    id("com.ryandens.example.helidon-conventions")
    id("com.ryandens.example.jlink-helidon-conventions")
    id("com.google.cloud.tools.jib")
    id("com.ryandens.jlink-jib")
    id("com.ryandens.temurin-binaries-repository")
    id("com.ryandens.javaagent-jib")
}

val jmods by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
    isCanBeDeclared = true
}

val copyJmods =
    tasks.register<Copy>("copyJdks") {
        from(provider { tarTree(jmods.singleFile) })
        into(project.layout.buildDirectory.dir("jdks"))
    }

dependencies {
    jmods("temurin26-binaries:OpenJDK26U-jmods_aarch64_linux_hotspot_26_35:jdk-26+35@tar.gz")
    javaagent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.27.0")
}

jib.container {
    mainClass = "io.helidon.microprofile.cdi.Main"
    environment =
        mapOf(
            "OTEL_TRACES_EXPORTER" to "logging",
            "OTEL_METRICS_EXPORTER" to "logging",
            "OTEL_LOGS_EXPORTER" to "logging",
        )
}

listOf(tasks.jibDockerBuild, tasks.jibBuildTar, tasks.jib).forEach { jibTask ->
    jibTask {
        // this dependsOn not needed locally, but was on a GitHub Actions ubuntu runner
        dependsOn(tasks.processJandexIndex)
        notCompatibleWithConfigurationCache("Jib is not compatible with configuration cache")
    }
}

jib.from {
    image = "gcr.io/distroless/java-base-debian13:nonroot-arm64@sha256:2829fac2eca538aaf6dca9bee7f31ec99dc9a7a52767c0817e78a8b4c82482ee"
}

val linuxJlinkJre =
    project.tasks.register<JlinkJreTask>("linuxJlinkJre") {
        // by default, JlinkJreTask uses the module path associated with the configured java toolchain that is executing
        // the jlink command, but this can be overridden to instead point at a different jmods directory for the purpose
        // of building a JRE for a different platform
        this.modulePath.fileProvider(copyJmods.map { File(it.destinationDir, "jdk-26+35-jmods/") })
        outputDirectory.set(file(layout.buildDirectory.dir("jlink-jre-linux")))
    }

jlinkJib {
    // by default, this plugin will include the JRE built by the jlinkJre task. Optionally, any directory can be used.
    // this enables the default jlinkJre task to build a JRE for one platform and a custom JlinkJreTask to build a JRE
    // for another platform
    jlinkJre.value(linuxJlinkJre.map { it.outputDirectory.get() })
}
