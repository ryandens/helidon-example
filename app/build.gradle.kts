plugins {
    id("com.ryandens.example.helidon-application-conventions")
    id("com.ryandens.example.helidon-jib-conventions")
    id("com.ryandens.example.mockito-conventions")
}

dependencies {
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-mocking")
    testImplementation("org.mockito:mockito-core")
    testJavaagent("org.mockito:mockito-core")
}
