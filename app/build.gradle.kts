plugins {
    id("com.ryandens.example.helidon-application-conventions")
    id("com.ryandens.example.helidon-jib-conventions")
    id("com.ryandens.example.mockito-conventions")
}

dependencies {
    compileOnly(libs.autoDelegate.annotations)
    annotationProcessor(libs.autoDelegate.processor)
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-mocking")
    testImplementation("org.mockito:mockito-core")
    testJavaagent("org.mockito:mockito-core")
}
