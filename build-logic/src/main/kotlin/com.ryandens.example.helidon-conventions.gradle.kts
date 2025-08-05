plugins {
    id("com.ryandens.example.java-common-conventions")
    id("com.github.vlsi.jandex")
}

dependencies {
    implementation(enforcedPlatform("io.helidon:helidon-dependencies:4.2.5"))
    implementation("io.helidon.microprofile.bundles:helidon-microprofile")
    implementation("org.glassfish.jersey.media:jersey-media-json-binding")
    runtimeOnly("io.smallrye:jandex")
    runtimeOnly("jakarta.activation:jakarta.activation-api")
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-junit5")
    testImplementation("org.assertj:assertj-core:3.27.3")
}
