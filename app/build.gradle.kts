plugins {
    id("com.ryandens.example.helidon-application-conventions")
    id("com.ryandens.example.helidon-jib-conventions")
    id("com.ryandens.example.mockito-conventions")
}

dependencies {
    val autoDelegateVersion = "0.3.1"
    compileOnly("com.ryandens:auto-delegate-annotations:$autoDelegateVersion")
    annotationProcessor("com.ryandens:auto-delegate-processor:$autoDelegateVersion")
    testImplementation("io.helidon.microprofile.testing:helidon-microprofile-testing-mocking")
    testImplementation("org.mockito:mockito-core")
    testJavaagent("org.mockito:mockito-core")
}
