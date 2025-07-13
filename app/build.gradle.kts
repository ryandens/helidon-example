plugins {
    id("com.ryandens.example.helidon-application-conventions")
    id("com.ryandens.example.helidon-jib-conventions")
}

dependencies {
    testImplementation(platform("org.mockito:mockito-bom:5.18.0"))
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-mocking")
    testImplementation("org.mockito:mockito-core")
}
