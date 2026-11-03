import com.ryandens.jlink.tasks.JlinkJreTask
import org.gradle.api.tasks.Copy

plugins {
    id("com.ryandens.example.helidon-conventions")
    id("com.ryandens.example.jlink-helidon-conventions")
    id("com.google.cloud.tools.jib")
    id("com.ryandens.jlink-jib")
    id("com.ryandens.javaagent-jib")
}

val jmods =
    configurations.create("jmods") {
        isCanBeResolved = true
        isCanBeConsumed = false
        isCanBeDeclared = true
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
            attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named("jmods"))
        }
    }

val copyJmods =
    tasks.register<Copy>("copyJdks") {
        group = "build"
        description = "Extracts Linux JDK modules for the container runtime."
        from(provider { tarTree(jmods.singleFile) })
        into(project.layout.buildDirectory.dir("jdks"))
    }

dependencies {
    add(jmods.name, "temurin27-binaries:OpenJDK27U-jmods_aarch64_linux_hotspot_27.0.1.0.2_1:jdk-27.0.1.0.2+1@tar.gz")
    javaagent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.33.1")
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
        inputs.files(sourceSets.main.map { it.output })
        notCompatibleWithConfigurationCache("Jib is not compatible with configuration cache")
    }
}

jib.from {
    image = "gcr.io/distroless/java-base-debian13:nonroot-arm64@sha256:2829fac2eca538aaf6dca9bee7f31ec99dc9a7a52767c0817e78a8b4c82482ee"
}

val linuxJlinkJre =
    project.tasks.register<JlinkJreTask>("linuxJlinkJre") {
        group = "build"
        description = "Builds the Linux runtime used by the container image."
        // by default, JlinkJreTask uses the module path associated with the configured java toolchain that is executing
        // the jlink command, but this can be overridden to instead point at a different jmods directory for the purpose
        // of building a JRE for a different platform
        this.modulePath.fileProvider(copyJmods.map { File(it.destinationDir, "jdk-27.0.1.0.2+1-jmods/") })
        outputDirectory.set(layout.buildDirectory.dir("jlink-jre-linux"))
    }

jlinkJib {
    // by default, this plugin will include the JRE built by the jlinkJre task. Optionally, any directory can be used.
    // this enables the default jlinkJre task to build a JRE for one platform and a custom JlinkJreTask to build a JRE
    // for another platform
    jlinkJre.value(linuxJlinkJre.flatMap { it.outputDirectory })
}
