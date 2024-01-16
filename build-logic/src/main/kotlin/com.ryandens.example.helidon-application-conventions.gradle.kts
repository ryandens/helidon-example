plugins {
    id("com.ryandens.example.java-application-conventions")
    id("com.ryandens.example.weld-conventions")
    id("com.github.vlsi.jandex")
    id("com.ryandens.jlink-application")
}

dependencies {
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
