import com.ryandens.jlink.tasks.JlinkJreTask
import org.gradle.api.tasks.Copy

plugins {
    id("com.ryandens.example.helidon-conventions")
    id("com.ryandens.example.jlink-helidon-conventions")
    id("com.google.cloud.tools.jib")
    id("com.ryandens.jlink-jib")
    id("com.ryandens.temurin-binaries-repository")
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
    jmods("temurin25-binaries:OpenJDK25U-jmods_aarch64_linux_hotspot_25.0.2_10:jdk-25.0.2+10@tar.gz")
}

jib.container {
    mainClass = "io.helidon.microprofile.cdi.Main"
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
        this.modulePath.fileProvider(copyJmods.map { File(it.destinationDir, "jdk-25.0.2+10-jmods/") })
        outputDirectory.set(file(layout.buildDirectory.dir("jlink-jre-linux")))
    }

jlinkJib {
    // by default, this plugin will include the JRE built by the jlinkJre task. Optionally, any directory can be used.
    // this enables the default jlinkJre task to build a JRE for one platform and a custom JlinkJreTask to build a JRE
    // for another platform
    jlinkJre.value(linuxJlinkJre.map { it.outputDirectory.get() })
}
